/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.extension.input.transport.tcp;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.tcp.transport.TcpNettyClient;

import java.util.ArrayList;

public class TcpInputTransportTestCase {
    static final Logger log = Logger.getLogger(TcpInputTransportTestCase.class);
    private volatile int count;
    private volatile boolean eventArrived;

    @Before
    public void init() {
        count = 0;
        eventArrived = false;
    }


    @Test
    public void testTcpInputTransport1() throws InterruptedException {
        log.info("tcpInputTransport TestCase 1");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" +
                "@source(type='tcp', @map(type='passThrough'))" +
                "define stream inputStream (a string, b int, c float, d long, e double, f bool);";
        String query = ("@info(name = 'query1') " +
                "from inputStream " +
                "select *  " +
                "insert into outputStream;");
        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(inStreamDefinition + query);

        executionPlanRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
//                for (Event event : inEvents) {
//                    count++;
//                    switch (count) {
//                        case 1:
//                            Assert.assertEquals(36.0, event.getData(0));
//                            break;
//                        case 2:
//                            Assert.assertEquals(36.0, event.getData(0));
//                            break;
//                        case 3:
//                            Assert.assertEquals(36.0, event.getData(0));
//                            break;
//                        case 4:
//                            Assert.assertEquals(36.0, event.getData(0));
//                            break;
//                        case 5:
//                            Assert.assertEquals(36.0, event.getData(0));
//                            break;
//                        case 6:
//                            Assert.assertEquals(36.0, event.getData(0));
//                            break;
//                        default:
//                            org.junit.Assert.fail();
//                    }
//                }
            }
        });

        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
        executionPlanRuntime.start();

        inputHandler.send(new Object[]{"test", 36, 3.0f, 380l, 23.0, true});
        inputHandler.send(new Object[]{"test1", 361, 31.0f, 3801l, 231.0, false});
        inputHandler.send(new Object[]{"test2", 362, 32.0f, 3802l, 232.0, true});

        TcpNettyClient tcpNettyClient = new TcpNettyClient();
        tcpNettyClient.connect("localhost", 8080);
        ArrayList<Event> arrayList = new ArrayList<Event>(3);

        arrayList.add(new Event(System.currentTimeMillis(), new Object[]{"test", 36, 3.0f, 380l, 23.0, true}));
        arrayList.add(new Event(System.currentTimeMillis(), new Object[]{"test1", 361, 31.0f, 3801l, 231.0, false}));
        arrayList.add(new Event(System.currentTimeMillis(), new Object[]{"test2", 362, 32.0f, 3802l, 232.0, true}));
        tcpNettyClient.send("inputStream", arrayList.toArray(new Event[3]));

        tcpNettyClient.disconnect();
        tcpNettyClient.shutdown();
        Thread.sleep(300);
//        Assert.assertEquals(6, count);
        Assert.assertTrue(eventArrived);
        executionPlanRuntime.shutdown();

    }

//    @Test
//    public void testMinForeverAggregatorExtension2() throws InterruptedException {
//        log.info("minForeverAggregator TestCase 2");
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        String inStreamDefinition = "define stream inputStream (price1 int,price2 int, price3 int);";
//        String query = ("@info(name = 'query1') from inputStream " +
//                "select minForever(price1) as minForeverValue " +
//                "insert into outputStream;");
//        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(inStreamDefinition + query);
//
//        executionPlanRuntime.addCallback("query1", new QueryCallback() {
//            @Override
//            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
//                EventPrinter.print(timeStamp, inEvents, removeEvents);
//                eventArrived = true;
//                for (Event event : inEvents) {
//                    count++;
//                    switch (count) {
//                        case 1:
//                            Assert.assertEquals(36, event.getData(0));
//                            break;
//                        case 2:
//                            Assert.assertEquals(36, event.getData(0));
//                            break;
//                        case 3:
//                            Assert.assertEquals(9, event.getData(0));
//                            break;
//                        default:
//                            org.junit.Assert.fail();
//                    }
//                }
//            }
//        });
//
//        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
//        executionPlanRuntime.start();
//
//        inputHandler.send(new Object[]{36, 38, 74});
//        inputHandler.send(new Object[]{78, 38, 37});
//        inputHandler.send(new Object[]{9, 39, 38});
//
//        Thread.sleep(300);
//        Assert.assertEquals(3, count);
//        Assert.assertTrue(eventArrived);
//        executionPlanRuntime.shutdown();
//
//    }
//
//
//    @Test
//    public void testMinForeverAggregatorExtension3() throws InterruptedException {
//        log.info("minForeverAggregator TestCase 3");
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        String inStreamDefinition = "define stream inputStream (price1 float, price2 float, price3 float);";
//        String query = ("@info(name = 'query1') from inputStream " +
//                "select minForever(price1) as minForeverValue " +
//                "insert into outputStream;");
//        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(inStreamDefinition + query);
//
//        executionPlanRuntime.addCallback("query1", new QueryCallback() {
//            @Override
//            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
//                EventPrinter.print(timeStamp, inEvents, removeEvents);
//                eventArrived = true;
//                for (Event event : inEvents) {
//                    count++;
//                    switch (count) {
//                        case 1:
//                            Assert.assertEquals(36f, event.getData(0));
//                            break;
//                        case 2:
//                            Assert.assertEquals(36f, event.getData(0));
//                            break;
//                        case 3:
//                            Assert.assertEquals(36f, event.getData(0));
//                            break;
//                        case 4:
//                            Assert.assertEquals(36f, event.getData(0));
//                            break;
//                        case 5:
//                            Assert.assertEquals(36f, event.getData(0));
//                            break;
//                        case 6:
//                            Assert.assertEquals(36f, event.getData(0));
//                            break;
//                        default:
//                            org.junit.Assert.fail();
//                    }
//                }
//            }
//        });
//
//        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
//        executionPlanRuntime.start();
//
//        inputHandler.send(new Object[]{36f, 36.75, 35.75});
//        inputHandler.send(new Object[]{37.88f, 38.12, 37.62});
//        inputHandler.send(new Object[]{39.00f, 39.25, 38.62});
//        inputHandler.send(new Object[]{36.88f, 37.75, 36.75});
//        inputHandler.send(new Object[]{38.12f, 38.12, 37.75});
//        inputHandler.send(new Object[]{38.12f, 40, 37.75});
//
//        Thread.sleep(300);
//        Assert.assertEquals(6, count);
//        Assert.assertTrue(eventArrived);
//        executionPlanRuntime.shutdown();
//
//    }
//
//    @Test
//    public void testMinForeverAggregatorExtension4() throws InterruptedException {
//        log.info("minForeverAggregator TestCase 4");
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        String inStreamDefinition = "define stream inputStream (price1 long, price2 long, price3 long);";
//        String query = ("@info(name = 'query1') from inputStream " +
//                "select minForever(price1) as minForever " +
//                "insert into outputStream;");
//        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(inStreamDefinition + query);
//
//        executionPlanRuntime.addCallback("query1", new QueryCallback() {
//            @Override
//            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
//                EventPrinter.print(timeStamp, inEvents, removeEvents);
//                eventArrived = true;
//                for (Event event : inEvents) {
//                    count++;
//                    switch (count) {
//                        case 1:
//                            Assert.assertEquals(36l, event.getData(0));
//                            break;
//                        case 2:
//                            Assert.assertEquals(36l, event.getData(0));
//                            break;
//                        case 3:
//                            Assert.assertEquals(9l, event.getData(0));
//                            break;
//                        default:
//                            org.junit.Assert.fail();
//                    }
//                }
//            }
//        });
//
//        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
//        executionPlanRuntime.start();
//
//        inputHandler.send(new Object[]{36l, 38, 74});
//        inputHandler.send(new Object[]{78l, 38, 37});
//        inputHandler.send(new Object[]{9l, 39, 38});
//
//        Thread.sleep(300);
//        Assert.assertEquals(3, count);
//        Assert.assertTrue(eventArrived);
//        executionPlanRuntime.shutdown();
//
//    }
//
//
//    @Test(expected = ExecutionPlanValidationException.class)
//    public void testMinForeverAggregatorExtension5() throws InterruptedException {
//        log.info("minForeverAggregator TestCase 5");
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        String inStreamDefinition = "define stream inputStream (price1 int,price2 double, price3 double);";
//        String query = ("@info(name = 'query1') from inputStream " +
//                "select minForever(price1, price2, price3) as minForeverValue " +
//                "insert into outputStream;");
//        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(inStreamDefinition + query);
//    }

}
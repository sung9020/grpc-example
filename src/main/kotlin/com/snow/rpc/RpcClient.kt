package com.snow.rpc

import HelloServiceGrpc
import io.grpc.ManagedChannelBuilder


/*
*
* @author snow
* @since 2021/03/17
*/
fun main(args: Array<String>){
	println("starting.. snow client")
	val channel = ManagedChannelBuilder
		.forAddress("localhost", 8888)
		.usePlaintext()
		.build()

	val stub = HelloServiceGrpc.newBlockingStub(channel)
	val response = stub.sayHello(getHelloRequest("ping", 1L))
	println("response = ${response.pong},${response.id}")
}

fun getHelloRequest(ping: String, id: Long): Hello.MessageRequest {
	return Hello.MessageRequest.newBuilder()
		.setPing(ping)
		.setId(id)
		.build()
}
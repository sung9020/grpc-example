package com.snow.rpc
import HelloServiceGrpc
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.lang.Thread.sleep

/*
*
* @author snow
* @since 2021/03/16
*/
fun main(args: Array<String>){
	println("starting.. snow server")
	val service = HelloService()
	val server = ServerBuilder
		.forPort(8888)
		.addService(service)
		.build()

	server.start()
	server.awaitTermination()
}

class HelloService : HelloServiceGrpc.HelloServiceImplBase() {
	override fun sayHello(request: Hello.MessageRequest?, responseObserver: StreamObserver<Hello.MessageResposne>?) {
		println("Hello snow ${request?.ping}, ${request?.id}")
		val response = Hello.MessageResposne.newBuilder().setPong(request?.ping).build()
		responseObserver?.onNext(response)
		responseObserver?.onCompleted()
	}

	override fun lotsOfResponse(
		request: Hello.MessageRequest?,
		responseObserver: StreamObserver<Hello.MessageResposne>?
	) {
		println("snow lotsOfResponse")

		for(i in 0..5){
			val response = Hello.MessageResposne
				.newBuilder()
				.setPong("hello snow id: $i")
				.build()
			responseObserver?.onNext(response)
			sleep(100)
		}
		responseObserver?.onCompleted()
	}
}

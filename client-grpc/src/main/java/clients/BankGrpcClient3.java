package clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import stubs.Bank;
import stubs.BankServiceGrpc;

import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcClient3 {
    public static void main(String args[]) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5555)
                .usePlaintext() //model de negociation
                .build();
        BankServiceGrpc.BankServiceStub aycStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("mad")
                .setCurrencyTo("USD")
                .setAmount(25)
                .build();

        StreamObserver<Bank.ConvertCurrencyRequest> performed = aycStub.performCurrencyStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("*************************");
                System.out.println(convertCurrencyResponse);
                System.out.println("*************************");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("END");
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                Bank.ConvertCurrencyRequest currencyRequest = Bank.ConvertCurrencyRequest.newBuilder()
                        .setAmount(Float.parseFloat(String.valueOf(Math.random()*7000)))
                        .build();
                performed.onNext(currencyRequest);
                System.out.println("counter =================> " + counter);
                ++counter;
                if(counter == 20){
                    performed.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);
        System.out.println("----- waiting");
        System.in.read();
    }
}

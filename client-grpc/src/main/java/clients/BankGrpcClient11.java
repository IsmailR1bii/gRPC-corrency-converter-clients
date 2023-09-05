package clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import stubs.Bank;
import stubs.BankServiceGrpc;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;

public class BankGrpcClient11 {
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

        aycStub.convert(request, new StreamObserver<Bank.ConvertCurrencyResponse>() { // callbacks
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("***************************");
                System.out.println(convertCurrencyResponse);
                System.out.println("***************************");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("....END");
            }
        });
        System.out.println("----- waiting");
        System.in.read();
    }
}

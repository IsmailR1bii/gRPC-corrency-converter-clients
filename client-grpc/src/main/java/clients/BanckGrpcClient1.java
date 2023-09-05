package clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import stubs.Bank;
import stubs.BankServiceGrpc;

public class BanckGrpcClient1 {
    public static void main(String args[]){
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5555)
                .usePlaintext() //mode de negociation
                .build();
        BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel); // proxy
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("mad")
                .setCurrencyTo("USD")
                .setAmount(25)
                .build();
        Bank.ConvertCurrencyResponse currencyResponse = blockingStub.convert(request);
        System.out.println(currencyResponse);
    }
}

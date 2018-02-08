package ao.Request;

import ao.Future.PutResult;
import ao.Buffor;

import java.util.ArrayList;

public class PutRequest implements MethodRequest {
    private Buffor buffor;
    private PutResult result;
    private int amount;

    public PutRequest(Buffor buffor, PutResult result, int amount){
        this.buffor = buffor;
        this.result = result;
        this.amount = amount;
    }
    @Override
    public boolean guard() {
        return (buffor.getOccupiedSpaces() + amount <= buffor.getCapacity());
    }

    @Override
    public void execute() {
        buffor.put(amount);
        result.setState(true);
    }
}

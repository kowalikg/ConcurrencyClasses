package ao.Request;

import ao.Future.GetResult;
import ao.Buffor;

public class GetRequest implements MethodRequest {
    private Buffor buffor;
    private GetResult result;
    private int amount;
    public GetRequest(Buffor buffor, GetResult result, int amount){
        this.buffor = buffor;
        this.result = result;
        this.amount = amount;
    }
    @Override
    public boolean guard() {
        return (buffor.getOccupiedSpaces() - amount >= 0);
    }

    @Override
    public void execute() {
        result.setValues(buffor.get(amount));
    }
}

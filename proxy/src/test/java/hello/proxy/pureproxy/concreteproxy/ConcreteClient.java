package hello.proxy.pureproxy.concreteproxy;

public class ConcreteClient {

	private ConcreteLogic concreteLogic;

	public ConcreteClient(ConcreteLogic concreteLogic) {
		this.concreteLogic = concreteLogic;
	}

	public void execute(){
		concreteLogic.operation();
	}
}

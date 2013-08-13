package main;

public class Main {

	static model.Model theModel;
	static view.View theView;
	static controller.Controller theController;
	
	public static void main(String[] args){
		theModel = new model.Model();
		theView = new view.View(theModel);
		theController = new controller.Controller(theModel, theView);
	}
}

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NP {

	static File f;
	boolean changed=false;
	String titleFile = "untitled";
	int count;

	NP() {

		MenuBar menubar = new MenuBar();

		SeparatorMenuItem separate= new SeparatorMenuItem();

		Menu file = new Menu("File");

		MenuItem New = new MenuItem("New");
		MenuItem NewWindow = new MenuItem("New Window");
		MenuItem Open = new MenuItem("Open...");
		MenuItem Save = new MenuItem("Save");
		MenuItem SaveAs = new MenuItem("Save as...");
		MenuItem Print = new MenuItem("Print...");
		MenuItem Ext = new MenuItem("Exit");

		file.getItems().addAll(New, NewWindow, Open, Save, SaveAs,Print, Ext);

		Menu edit = new Menu("Edit");

		MenuItem Redo = new MenuItem("Redo");
		MenuItem Undo = new MenuItem("Undo");
		MenuItem Copy = new MenuItem("Copy");
		MenuItem Cut = new MenuItem("Cut");
		MenuItem Paste = new MenuItem("Paste");
		MenuItem Delete = new MenuItem("Delete");
		MenuItem Find = new MenuItem("Find");
		MenuItem Replace = new MenuItem("Replace");
		MenuItem Select = new MenuItem("Select All");

		edit.getItems().addAll(Redo, Undo,separate, Copy, Cut, Paste, Delete, Find, Replace,Select);

		Menu format = new Menu("Format");

		CheckMenuItem Wordwrap = new CheckMenuItem("Word Wrap");
		MenuItem Font = new MenuItem("Font");

		format.getItems().addAll(Wordwrap, Font);

		Menu view = new Menu("View");

		MenuItem Z_in = new MenuItem("Zoom in");
		MenuItem Z_out = new MenuItem("Zoom out");

		view.getItems().addAll(Z_in, Z_out);

		Menu help = new Menu("Help");

		MenuItem Contact = new MenuItem("Contact us");
		MenuItem About = new MenuItem("About us");

		help.getItems().addAll(Contact, About);

		menubar.getMenus().addAll(file, edit, format, view, help);


		TextArea txtArea = new TextArea();
		txtArea.setId("txt-area");

		Label lbl1 = new Label();

		HBox hbox = new HBox();
		hbox.getChildren().addAll(lbl1);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.setPadding(new Insets(5,8,5,8));



		GridPane gp = new GridPane();
		gp.add(menubar, 0, 0);
		gp.add(txtArea, 0, 1);
		gp.add(hbox, 0, 2);

		GridPane.setHgrow(txtArea, Priority.ALWAYS);
		GridPane.setVgrow(txtArea, Priority.ALWAYS);
		GridPane.setHgrow(menubar, Priority.ALWAYS);

		Scene scene = new Scene(gp);
		scene.getStylesheets().add(NP.class.getResource("application.css").toExternalForm());
		Stage stg = new Stage();
		stg.setScene(scene);
		stg.setHeight(400.5);
		stg.setWidth(720.7);

		stg.setTitle(titleFile + " - " + "NPtext");
		stg.getIcons().add(new Image(getClass().getResourceAsStream("notes.png")));


		txtArea.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				count=txtArea.getText().length();
				lbl1.setText(count+" charachters");
				changed=true;
			}

		});

		NewWindow.setOnAction(ActionEvent ->{
			callNp();
		});

		Open.setOnAction(ActionEvent -> {
			FileChooser fileChooser = new FileChooser();

			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));

			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

			File fileToLoad = fileChooser.showOpenDialog(null);

			Scanner read = null;

			f = fileToLoad;

			if (f != null) {
				titleFile = f.getName().toString();
				stg.setTitle(titleFile + " - " + "NPtext");
			}
			try {
				read = new Scanner(fileToLoad);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// if file has been chosen, load it using asynchronous method (define later)
			txtArea.clear();
			if (fileToLoad != null) {
				while (read.hasNextLine()) {
					txtArea.appendText(read.nextLine() + "\n ");
				}
				changed=false;
			}
		});

		SaveAs.setOnAction(ActionEvent -> {
			FileChooser fch = new FileChooser();
			fch.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
			fch.setInitialDirectory(new File(System.getProperty("user.home")));
			File savefile = fch.showSaveDialog(null);
			f = savefile;

			if (f != null) {
				titleFile = f.getName().toString();
				stg.setTitle(titleFile + " - " + "NPtext");
			}
			try {
				FileWriter filewriter = new FileWriter(savefile);
				filewriter.write(txtArea.getText());
				filewriter.close();
				changed=false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Save.setOnAction(ActionEvent -> {

			if (f == null) {
				f = ChooseFile();;

				if (f != null) {
					titleFile = f.getName().toString();
					stg.setTitle(titleFile + " - " + "NPtext");
				}
			}

			try {
				FileWriter filewriter = new FileWriter(f);
				filewriter.write(txtArea.getText());
				filewriter.close();
				changed=false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Print.setOnAction(ActionEvent ->{
			PrinterJob printerJob = PrinterJob.createPrinterJob(); 
			if (printerJob != null) { 
				boolean printed = printerJob.printPage(txtArea); 
				if (printed) { 
					printerJob.endJob(); 
				} else { 
					System.out.println("Printing failed."); 
				} 
			} else { 
				System.out.println("Could not create a printer job."); 
			} 
		});



		//dialog box
		Label txt = new Label("Do you want to save the changes to "+titleFile);
		Button btn1 = new Button("Save");
		Button btn2 = new Button("Dont Save");
		Button btn3 = new Button("Cancel");		

		GridPane xgp = new GridPane();

		xgp.setPadding(new Insets(10, 10, 10, 10));
		txt.setPadding(new Insets(10, 5, 10, 5));

		xgp.setVgap(5);
		xgp.setHgap(5);



		xgp.add(txt, 0, 0,4,1);
		xgp.add(btn1, 1, 1,1,1);
		xgp.add(btn2, 2, 1,1,1);
		xgp.add(btn3, 3, 1,1,1);


		Scene xScene = new Scene(xgp, 250, 150);

		Stage xStg = new Stage();
		xStg.setScene(xScene);
		xStg.setWidth(330);
		xStg.setHeight(120);
		xStg.setTitle("NPtext");

		Button nbtn1 = new Button("Save");
		Button nbtn2 = new Button("Dont Save");
		Button nbtn3 = new Button("Cancel");

		GridPane ngp = new GridPane();

		ngp.setPadding(new Insets(10, 10, 10, 10));

		ngp.setVgap(5);
		ngp.setHgap(5);		
		ngp.add(txt, 0, 0,4,1);
		ngp.add(nbtn1, 1, 1,1,1);
		ngp.add(nbtn2, 2, 1,1,1);
		ngp.add(nbtn3, 3, 1,1,1);

		Scene nScene = new Scene(ngp, 250, 150);


		Stage nStg = new Stage();
		nStg.setScene(nScene);
		nStg.setWidth(330);
		nStg.setHeight(120);
		nStg.setTitle("NPtext");


		btn1.setOnAction(ActionEvent ->{
			if (f == null) {
				f = ChooseFile();;

				if (f != null) {
					titleFile = f.getName().toString();
					stg.setTitle(titleFile + " - " + "NPtext");
				}
			}

			try {
				FileWriter filewriter = new FileWriter(f);
				filewriter.write(txtArea.getText());
				filewriter.close();
				changed=false;
			} catch (IOException e) {
				e.printStackTrace();
			}

			Platform.exit();	
		});

		nbtn1.setOnAction(ActionEvent ->{
			if (f == null) {
				f = ChooseFile();;

				if (f != null) {
					titleFile = f.getName().toString();
					stg.setTitle(titleFile + " - " + "NPtext");
				}
			}

			try {
				FileWriter filewriter = new FileWriter(f);
				filewriter.write(txtArea.getText());
				filewriter.close();
				changed=false;
			} catch (IOException e) {
				e.printStackTrace();
			}

			txtArea.clear();
			f=null;
			stg.setTitle("untitled" + " - " + "NPtext");

			nStg.close();
		});

		btn2.setOnAction(ActionEvent ->{
			Platform.exit();
		});

		nbtn2.setOnAction(ActionEvent ->{
			txtArea.clear();
			f=null;
			stg.setTitle("untitled" + " - " + "NPtext");
			nStg.close();
		});

		btn3.setOnAction(ActionEvent ->{
			xStg.close();
		});

		nbtn3.setOnAction(ActionEvent ->{
			nStg.close();
		});


		New.setOnAction(ActionEvent ->{
			if(changed==true) {
				nStg.show();
			}else {
				txtArea.clear();
				f=null;
				stg.setTitle("untitled" + " - " + "NPtext");

			}
		});

		Ext.setOnAction(ActionEvent -> {
			if(changed==true) {
				xStg.show();

			}else {
				Platform.exit();;
			}
		});


		Redo.setOnAction(ActionEvent ->{
			txtArea.redo();
		});

		Undo.setOnAction(ActionEvent ->{
			txtArea.undo();
		});

		Copy.setOnAction(ActionEvent -> {
			final Clipboard clipboard = Clipboard.getSystemClipboard();
			final ClipboardContent content = new ClipboardContent();
			content.putString(txtArea.getSelectedText());
			clipboard.setContent(content);
		});

		Cut.setOnAction(ActionEvent ->{
			txtArea.cut();
		});

		Paste.setOnAction(ActionEvent ->{
			txtArea.paste();
		});

		Delete.setOnAction(ActionEvent ->{
			txtArea.replaceSelection("");
		});

		Select.setOnAction(ActionEvent ->{
			txtArea.selectAll();
		});

		Wordwrap.setOnAction(ActionEvent ->{
			if(Wordwrap.isSelected()) {
				txtArea.setWrapText(true);
			}else {
				txtArea.setWrapText(false);
			}
		});

		Font.setOnAction(ActionEvent ->{

		});

		Z_in.setOnAction(ActionEvent ->{

		});

		Z_out.setOnAction(ActionEvent ->{

		});

		Contact.setOnAction(ActionEvent ->{

		});

		About.setOnAction(ActionEvent ->{
			AnchorPane aPane = new AnchorPane();
			Scene aScene = new Scene(aPane);
			Stage aStg = new Stage();
			aStg.setScene(aScene);
			aStg.setWidth(300);
			aStg.setHeight(380);
			aStg.setTitle("About us");
			aStg.show();
		});
		stg.show();

	}

	static void callNp() {
		NP np = new NP();	
	}

	static File ChooseFile(){
		FileChooser fch = new FileChooser();
		fch.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
		fch.setInitialDirectory(new File(System.getProperty("user.home")));
		File savefile = fch.showSaveDialog(null);
		return savefile;
	}

}

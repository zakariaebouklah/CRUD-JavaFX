package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class Controller implements Initializable{

    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_title;

    @FXML
    private TextField tf_author;

    @FXML
    private TextField tf_year;

    @FXML
    private TextField tf_pages;

    @FXML
    private TableView<Books> tv_books;

    @FXML
    private TableColumn<Books, Integer> col_id;

    @FXML
    private TableColumn<Books, String> col_title;

    @FXML
    private TableColumn<Books, String> col_author;

    @FXML
    private TableColumn<Books, Integer> col_year;

    @FXML
    private TableColumn<Books, Integer> col_pages;

    @FXML
    private Button btn_insert;

    @FXML
    private Button btn_update;

    @FXML
    private Button btn_remove;

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showBooks();
	}

    @FXML
    private void handleButtonAction(ActionEvent event) {
    	if (event.getSource() == btn_insert) {
			insertRecord();
		}
    	if (event.getSource() == btn_update) {
    		updateRecord();
		}
    	if (event.getSource() == btn_remove) {
			deleteRecord();
		}
    }

    public Connection getConnection(){
    	Connection conn;
    	try {
    		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library? useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			return conn;
		} catch (Exception e) {
			System.out.println("Cannot Connect : " + e.getMessage());
			return null;
		}
    }

    public ObservableList<Books> getBooksList(){
    	ObservableList<Books> bookList = FXCollections.observableArrayList();
    	Connection conn = getConnection();
    	String query = "SELECT * FROM books";
    	Statement st;
    	ResultSet rs;

    	try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Books books;

			while(rs.next()){
				books = new Books(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("year"), rs.getInt("pages"));
				bookList.add(books);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
    	return bookList;
    }

    public void showBooks(){
    	ObservableList<Books> list = getBooksList();

    	col_id.setCellValueFactory(new PropertyValueFactory<Books,Integer>("id"));
    	col_title.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
    	col_author.setCellValueFactory(new PropertyValueFactory<Books,String>("author"));
    	col_year.setCellValueFactory(new PropertyValueFactory<Books,Integer>("year"));
    	col_pages.setCellValueFactory(new PropertyValueFactory<Books,Integer>("pages"));

    	tv_books.setItems(list);
    }

    public void insertRecord(){
    	String query = "INSERT INTO books VALUES (" + tf_id.getText() + ",'" + tf_title.getText() + "','" + tf_author.getText() + "'," + tf_year.getText() + "," + tf_pages.getText() + ")";
    	executeQuery(query);
    	showBooks();
    }

    public void updateRecord(){
    	String query = "UPDATE books SET title = '" + tf_title.getText() + "', author = '" + tf_author.getText() + "', year = " + tf_year.getText() + ", pages = " + tf_pages.getText() + " WHERE id = " + tf_id.getText() + "";
    	executeQuery(query);
    	showBooks();
    }

    public void deleteRecord(){
    	String query = "DELETE FROM books WHERE id = " + tf_id.getText() + "";
    	executeQuery(query);
    	showBooks();
    }

	private void executeQuery(String query) {
		Connection conn = getConnection();
		Statement st;
		try {
			st = conn.createStatement();
			st.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
    void handleMouseClick(MouseEvent event) {
		Books book = tv_books.getSelectionModel().getSelectedItem();
		if(book!=null){
			tf_id.setText("" + book.getId());
			tf_title.setText("" + book.getTitle());
			tf_author.setText("" + book.getAuthor());
			tf_year.setText("" + book.getYear());
			tf_pages.setText("" + book.getPages());
		}else{
			tf_id.setText("");
			tf_title.setText("");
			tf_author.setText("");
			tf_year.setText("");
			tf_pages.setText("");
		}

    }
}

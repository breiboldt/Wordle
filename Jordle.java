import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.concurrent.atomic.AtomicBoolean;

/**.
 * @author breiboldt3@gatech.edu
 * @version 1.0
 * Application that mimics the Wordle, but is created entirely in javafx
 */
public class Jordle extends Application {
    /**.
     * Main method that houses all code for the application
     * @param args String[] representing arguments passed in
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        String word = Words.list.get((int) (Math.random() * Words.list.size()));
        //initiating nodes
        Label[] label = new Label[30];
        Label textLabel = new Label("Try guessing a word");
        textLabel.setStyle("-fx-fill: white");
        Rectangle[] rectangle = new Rectangle[30];
        for (int i = 0; i < 30; i++) {
            label[i] = new Label();
            label[i].setStyle("-fx-fill: black");
            label[i].setFont(Font.font("verdana", FontWeight.BOLD, 30));
            label[i].setPadding(new Insets(5, 5, 5, 28));
            rectangle[i] = new Rectangle(79, 79);
            rectangle[i].setStyle("-fx-fill: white; -fx-border-style: solid;"
                    + "-fx-border-width: 5; -fx-border-color: white;");
        }
        TextField guess = new TextField();
        Rectangle cover = new Rectangle(100, 50);
        Text title = new Text("   Wordle");
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        title.setFill(Color.WHITE);
        Button instr = new Button("Instructions");
        Button restart = new Button("Restart");
        //adding nodes
        root.add(title, 1, 0, 1, 1);
        root.addRow(1, rectangle[0], rectangle[1], rectangle[2], rectangle[3], rectangle[4]);
        root.addRow(2, rectangle[5], rectangle[6], rectangle[7], rectangle[8], rectangle[9]);
        root.addRow(3, rectangle[10], rectangle[11], rectangle[12], rectangle[13], rectangle[14]);
        root.addRow(4, rectangle[15], rectangle[16], rectangle[17], rectangle[18], rectangle[19]);
        root.addRow(5, rectangle[20], rectangle[21], rectangle[22], rectangle[23], rectangle[24]);
        root.addRow(6, rectangle[25], rectangle[26], rectangle[27], rectangle[28], rectangle[29]);
        root.add(guess, 4, 7);
        root.add(textLabel, 0, 7, 2, 1);
        root.add(instr, 2, 7);
        root.add(restart, 3, 7);
        root.add(cover, 4, 7);
        int j = 0;
        int k = 1;
        for (int i = 0; i < 30; i++) {
            root.add(label[i], j, k);
            if (j == 4) {
                j = 0;
                k++;
            } else {
                j++;
            }
        }
        //column and row sizing
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints(80);
            RowConstraints row = new RowConstraints(80);
            if (i < 5) {
                root.getColumnConstraints().add(column);
            }
            root.getRowConstraints().add(row);
        }
        //typing in text
        final int[] spot = {0};
        final int[] enter = {0};
        AtomicBoolean pass = new AtomicBoolean(false);
        AtomicBoolean game = new AtomicBoolean(true);
        guess.setOnKeyPressed(ke -> {
            if (ke.getCode().isLetterKey() && ((enter[0] * 5 <= spot[0]
                    && (enter[0] + 1) * 5 > spot[0]) || pass.get() || spot[0] == 0)
                    && spot[0] < 30 && game.get()) {
                label[spot[0]].setText(ke.getText().toUpperCase());
                spot[0]++;
                pass.set(false);
            } else if (ke.getCode().equals(KeyCode.BACK_SPACE) && spot[0] != 0
                    && rectangle[spot[0] - 1].getFill() == Color.WHITE) {
                label[spot[0] - 1].setText("");
                spot[0]--;
            } else if (ke.getCode().equals(KeyCode.ENTER)) {
                if ((spot[0]) % 5 == 0 && spot[0] != 0) {
                    for (int i = 0; i < 5; i++) {
                        if (Character.toString(word.charAt(4 - i)).toUpperCase()
                                .equals(label[spot[0] - i - 1].getText())) {
                            rectangle[spot[0] - i - 1].setFill(Color.GREEN);
                        } else if (word.toUpperCase().contains(label[spot[0] - i - 1].getText())) {
                            rectangle[spot[0] - i - 1].setFill(Color.YELLOW);
                        } else {
                            rectangle[spot[0] - i - 1].setFill(Color.GREY);
                        }
                    }
                    pass.set(true);
                    enter[0]++;
                    game.set(!(rectangle[spot[0] - 5].getFill() == Color.GREEN
                            && rectangle[spot[0] - 4].getFill() == Color.GREEN
                            && rectangle[spot[0] - 3].getFill() == Color.GREEN
                            && rectangle[spot[0] - 2].getFill() == Color.GREEN
                            && rectangle[spot[0] - 1].getFill() == Color.GREEN));
                    if (!game.get()) {
                        textLabel.setText("Congratulations, you win!");
                        game.set(false);
                    } else if (enter[0] == 6) {
                        textLabel.setText("Game over. The word was \n" + word + ".");
                        game.set(false);
                    }
                }
            }
        });
        //Instructions window
        Stage secondaryStage = new Stage();
        TextArea instructions = new TextArea("Welcome to Jordle!\n"
                + "To start, type in a valid 5 letter guess.\n"
                + "You have 6 guesses to guess the word.\n"
                + "If you guess a letter that is in the final word, it will turn yellow.\n"
                + "If the letter is also in the right spot, it will turn green.");
        Pane instruction = new Pane(instructions);
        Scene ins = new Scene(instruction);
        secondaryStage.setTitle("Instructions");
        secondaryStage.setScene(ins);
        secondaryStage.setMinWidth(400);
        secondaryStage.setMinHeight(200);
        instr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                secondaryStage.show();
            }
        });
        //restart button
        restart.setOnAction(e -> {
            Stage newStage = new Stage();
            primaryStage.close();
            start(newStage);
        });
        //GridPane setup
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(5);
        root.setHgap(5);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), Insets.EMPTY)));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Jordle");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }
}

package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    //Variables
    private Stage primaryStage;
    private AnchorPane openingPane;
    private Scene openingScene;
    private Popup PopUp;
    private Account[] accounts = Accounts.accounts;
    private int accountArrayLength = Accounts.accountArrayLength; // Initialize to 5 for the 5 demo accounts created
    private Game[] games = new Game[100];
    private int gameArrayLength = 1; // Initialize to 5 for the 5 demo accounts created
    private Account accountLoggedIn;
    private TabPane tabPane;
    private Tab homeTab;
    private Tab leagueTab;
    private Tab followingTab;
    private Tab myAdvertisementsTab;
    private MenuButton optionsBtn;
    private MenuItem defineGameBtn;
    private MenuItem changeTypeBtn;
    private MenuItem createLeagueBtn;

    // GUI Interface
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage; //sets this primaryStage as 'the' primaryStage
        openingPane = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        tabPane = (TabPane) openingPane.lookup("#tabPane");
        homeTab = tabPane.getTabs().get(0);
        leagueTab = tabPane.getTabs().get(1);
        followingTab = tabPane.getTabs().get(2);
        myAdvertisementsTab = tabPane.getTabs().get(3);
        tabPane.getTabs().remove(myAdvertisementsTab);
        // used to chose which options to display
        optionsBtn = (MenuButton) openingPane.lookup("#optionsBtn");
        defineGameBtn = optionsBtn.getItems().get(0);
        createLeagueBtn = optionsBtn.getItems().get(1);
        changeTypeBtn = optionsBtn.getItems().get(2);
        optionsBtn.getItems().remove(defineGameBtn);
        optionsBtn.getItems().remove(createLeagueBtn);
        optionsBtn.getItems().remove(changeTypeBtn);

        /**
         * MAIN STAGE CREATED
         * Main Stage and Scene Created and shown
         */
        openingScene = new Scene(openingPane); //creates a new scene from 'MainPage.fxml'
        primaryStage.setScene(openingScene); //sets the scene on the stage
        primaryStage.setAlwaysOnTop(false); //set to false to show popups
        primaryStage.setResizable(false); //makes app able to be resized
        primaryStage.show(); //shows the primaryStage
        newOrReturningUserPopUp(); // ask user if they are a new or returning user

        /** Create 5 demo accounts */
        // Demo operator account
        accounts[0] = AccountFactory.buildAccount(AccountType.OPERATOR);
        accounts[0].setEmail("operator@email.com");
        accounts[0].setUsername("operator");
        accounts[0].setPassword("password");
        // Demo league owner account
        accounts[1] = AccountFactory.buildAccount(AccountType.LEAGUEOWNER);
        accounts[1].setEmail("leagueowner@email.com");
        accounts[1].setUsername("leagueowner");
        accounts[1].setPassword("password");
        // Demo league owner account
        accounts[2] = AccountFactory.buildAccount(AccountType.PLAYER);
        accounts[2].setEmail("player@email.com");
        accounts[2].setUsername("player");
        accounts[2].setPassword("password");
        // Demo league owner account
        accounts[3] = AccountFactory.buildAccount(AccountType.SPECTATOR);
        accounts[3].setEmail("spectator@email.com");
        accounts[3].setUsername("spectator");
        accounts[3].setPassword("password");
        // Demo league owner account
        accounts[4] = AccountFactory.buildAccount(AccountType.ADVERTISER);
        accounts[4].setEmail("advertiser@email.com");
        accounts[4].setUsername("advertiser");
        accounts[4].setPassword("password");

        /** Create 1 demo game */
        games[0] = new Game.GameDefiner("TicTacToe", 2).requiredPoints(0).define();

        /**
         * TAB LISTENER
         * Listen for which tab is selected
         */
        leagueTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (leagueTab.isSelected() && accountLoggedIn != null) {
                    if (accountLoggedIn.getType().equals(AccountType.LEAGUEOWNER)) {
                        System.out.println("league");
                    }
                }
            }
        });

        defineGameBtn.setOnAction(event -> {
            defineGamePopUp();
        });

        changeTypeBtn.setOnAction(event -> {
            accountTypePopUp(1);
        });
    }

    /**
     * HIDE POPUP METHOD
     * Resumes the game
     */
    private void hidePopUp() {
        try {
            PopUp.hide();
            PopUp = null;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * NEW OR RETURNING USER POPUP
     * PopUp to ask user if they are new or not
     */
    private void newOrReturningUserPopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane newOrReturningUserPopUpPane = null; //calls popup menu created in 'newOrReturningUserPopUp.fxml' file

        try {
            newOrReturningUserPopUpPane = FXMLLoader.load(getClass().getResource("newOrReturningUserPopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(newOrReturningUserPopUpPane); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        openingPane.requestFocus();

        Button createAccountBtn = (Button) newOrReturningUserPopUpPane.lookup("#createAccount");
        createAccountBtn.setOnAction(event -> {
            hidePopUp();
            accountTypePopUp(0);

        });

        Button loginBtn = (Button) newOrReturningUserPopUpPane.lookup("#login");
        loginBtn.setOnAction(event -> {
            hidePopUp();
            accountLoginPopUp();
        });
    }

    /**
     * ACCOUNT TYPE POPUP
     * PopUp to ask user to select an account type
     */
    private void accountTypePopUp(int x) {
        PopUp = new Popup(); //creates new popup

        TitledPane accountTypePopUpPane = null; //calls popup menu created in 'accountTypePopUp.fxml' file

        try {
            accountTypePopUpPane = FXMLLoader.load(getClass().getResource("accountTypePopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(accountTypePopUpPane); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        openingPane.requestFocus();

        Button leagueOwnerBtn = (Button) accountTypePopUpPane.lookup("#leagueOwner");
        leagueOwnerBtn.setOnAction(event -> {
            hidePopUp();
            accounts[accountArrayLength] = AccountFactory.buildAccount(AccountType.LEAGUEOWNER);
            if (x == 0) {
                createAccountPopUp();
            } else if (x == 1) {
                if (Operator.approveAndChange(accountLoggedIn, AccountType.LEAGUEOWNER)) {
                    accountTypeChangedPopUp();
                } else {
                    accountTypeNotChangedPopUp();
                }
            }
        });

        Button playerBtn = (Button) accountTypePopUpPane.lookup("#player");
        playerBtn.setOnAction(event -> {
            hidePopUp();
            accounts[accountArrayLength] = AccountFactory.buildAccount(AccountType.PLAYER);
            if (x == 0) {
                createAccountPopUp();
            } else if (x == 1) {
                if (Operator.approveAndChange(accountLoggedIn, AccountType.PLAYER)) {
                    accountTypeChangedPopUp();
                } else {
                    accountTypeNotChangedPopUp();
                }
            }
        });

        Button spectatorBtn = (Button) accountTypePopUpPane.lookup("#spectator");
        spectatorBtn.setOnAction(event -> {
            hidePopUp();
            accounts[accountArrayLength] = AccountFactory.buildAccount(AccountType.SPECTATOR);
            if (x == 0) {
                createAccountPopUp();
            } else if (x == 1) {
                if (Operator.approveAndChange(accountLoggedIn, AccountType.SPECTATOR)) {
                    accountTypeChangedPopUp();
                } else {
                    accountTypeNotChangedPopUp();
                }
            }
        });

        Button advertiserBtn = (Button) accountTypePopUpPane.lookup("#advertiser");
        advertiserBtn.setOnAction(event -> {
            hidePopUp();
            accounts[accountArrayLength] = AccountFactory.buildAccount(AccountType.ADVERTISER);
            if (x == 0) {
                createAccountPopUp();
            } else if (x == 1) {
                if (Operator.approveAndChange(accountLoggedIn, AccountType.ADVERTISER)) {
                    accountTypeChangedPopUp();
                } else {
                    accountTypeNotChangedPopUp();
                }
            }
        });
    }

    /**
     * CREATE ACCOUNT POPUP
     * PopUp to create new account
     */
    public void createAccountPopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane createAccountPopUpPane = null; //calls popup menu created in 'createAccountPopUp.fxml' file

        try {
            createAccountPopUpPane = FXMLLoader.load(getClass().getResource("createAccountPopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(createAccountPopUpPane); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        TextField t1 = ((TextField) createAccountPopUpPane.lookup("#email"));
        t1.requestFocus();

        Button enterBtn = (Button) createAccountPopUpPane.lookup("#enter");
        TitledPane finalCreateAccountPopUpPane = createAccountPopUpPane;
        enterBtn.setOnAction(event -> {
            TextField email = new TextField();
            TextField username = new TextField();
            TextField password = new TextField();
            if (!(((TextField) finalCreateAccountPopUpPane.lookup("#email")).getText().equals(""))) {
                email = (TextField) finalCreateAccountPopUpPane.lookup("#email");
            } else {
                createAccountPopUp();
            }
            if (!(((TextField) finalCreateAccountPopUpPane.lookup("#username")).getText().equals(""))) {
                username = (TextField) finalCreateAccountPopUpPane.lookup("#username");
            }else {
                createAccountPopUp();
            }
            if (!(((TextField) finalCreateAccountPopUpPane.lookup("#password")).getText().equals(""))) {
                password = (TextField) finalCreateAccountPopUpPane.lookup("#password");
            }else {
                createAccountPopUp();
            }
            if (!Operator.verifyAccount(username.getText())) {
                hidePopUp();
                return;
            }

            if (!AccountController.checkCredentials(username, email, password)) {
                hidePopUp();
                usernameOrEmailAlreadyExistPopUp();
                return;
            }

            accounts[accountArrayLength].setEmail(email.getText());
            accounts[accountArrayLength].setUsername(username.getText());
            accounts[accountArrayLength].setPassword(password.getText());
            setLoggedInAccount(accountArrayLength);
            hidePopUp();
            accountArrayLength++;
        });
    }

    /**
     * USERNAME ALREADY EXISTS POPUP
     * PopUp for when a player tries to enter a username or email that already exist
     */
    private void usernameOrEmailAlreadyExistPopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane usernameAlreadyExistPopUpPane = null; //calls popup menu created in 'usernameOrEmailAlreadyExistPopUp.fxml' file

        try {
            usernameAlreadyExistPopUpPane = FXMLLoader.load(getClass().getResource("usernameOrEmailAlreadyExistPopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(usernameAlreadyExistPopUpPane); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        Button dismissBtn = (Button) usernameAlreadyExistPopUpPane.lookup("#dismiss");

        dismissBtn.setOnAction(event -> {
            hidePopUp();
            createAccountPopUp();
        });
    }

    /**
     * ACCOUNT LOGIN POPUP
     * PopUp to login to account
     */
    private void accountLoginPopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane accountLoginPopUpPane = null; //calls popup menu created in 'accountLoginPopUp.fxml' file

        try {
            accountLoginPopUpPane = FXMLLoader.load(getClass().getResource("accountLoginPopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(accountLoginPopUpPane); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        TextField t1 = ((TextField) accountLoginPopUpPane.lookup("#username"));
        t1.requestFocus();

        Button enterBtn = (Button) accountLoginPopUpPane.lookup("#enter");
        TitledPane finalCreateAccountPopUpPane = accountLoginPopUpPane;
        enterBtn.setOnAction(event -> {
            TextField username = new TextField();
            TextField password = new TextField();
            ;
            if (!(((TextField) finalCreateAccountPopUpPane.lookup("#username")).getText().equals(""))) {
                username = (TextField) finalCreateAccountPopUpPane.lookup("#username");
            } else {
                accountLoginPopUp();
            }
            if (!(((TextField) finalCreateAccountPopUpPane.lookup("#password")).getText().equals(""))) {
                password = (TextField) finalCreateAccountPopUpPane.lookup("#password");
            } else {
                accountLoginPopUp();
            }

            for (int i = 0; i < accountArrayLength; i++) {
                if (accounts[i].getUsername().equals(username.getText()) && accounts[i].getPassword().equals(password.getText())) {
                    setLoggedInAccount(i);
                    hidePopUp();
                    return;
                }
            }
            hidePopUp();
            incorrectLoginPopUp();
        });
    }

    /**
     * NAME TOO LONG POPUP
     * PopUp for when a player tries to enter an invalid username
     */
    private void incorrectLoginPopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane incorrectLoginPopUp = null; //calls popup menu created in 'incorrectLoginPopUp.fxml' file

        try {
            incorrectLoginPopUp = FXMLLoader.load(getClass().getResource("incorrectLoginPopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(incorrectLoginPopUp); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        Button dismissBtn = (Button) incorrectLoginPopUp.lookup("#dismiss");

        dismissBtn.setOnAction(event -> {
            hidePopUp();
            accountLoginPopUp();
        });
    }

    /**
     * ACCOUNT TYPE CHANGED POPUP
     * PopUp for when a account change is approved
     */
    private void accountTypeChangedPopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane accountTypeChangedPopUp = null; //calls popup menu created in 'accountTypeChangedPopUp.fxml' file

        try {
            accountTypeChangedPopUp = FXMLLoader.load(getClass().getResource("accountTypeChangedPopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(accountTypeChangedPopUp); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        Button dismissBtn = (Button) accountTypeChangedPopUp.lookup("#dismiss");

        dismissBtn.setOnAction(event -> {
            hidePopUp();
        });
    }

    /**
     * ACCOUNT TYPE NOT CHANGED POPUP
     * PopUp for when a account change is not approved
     */
    private void accountTypeNotChangedPopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane accountTypeNotChangedPopUp = null; //calls popup menu created in 'accountTypeNotChangedPopUp.fxml' file

        try {
            accountTypeNotChangedPopUp = FXMLLoader.load(getClass().getResource("accountTypeNotChangedPopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(accountTypeNotChangedPopUp); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        Button dismissBtn = (Button) accountTypeNotChangedPopUp.lookup("#dismiss");

        dismissBtn.setOnAction(event -> {
            hidePopUp();
        });
    }

    /**
     * DEFINE GAME POPUP
     * PopUp to define a new game
     */
    public void defineGamePopUp() {
        PopUp = new Popup(); //creates new popup

        TitledPane defineGamePopUp = null; //calls popup menu created in 'defineGamePopUp.fxml' file

        try {
            defineGamePopUp = FXMLLoader.load(getClass().getResource("defineGamePopUp.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopUp.getContent().add(defineGamePopUp); //adds the popup (child) created in fxml file to the popup (parent) created

        //show popup on primaryStage
        PopUp.show(primaryStage);

        TextField t1 = ((TextField) defineGamePopUp.lookup("#name"));
        t1.requestFocus();

        Button enterBtn = (Button) defineGamePopUp.lookup("#enter");
        TitledPane finalDefineGamePopUp = defineGamePopUp;
        enterBtn.setOnAction(event -> {
            TextField name = new TextField();
            TextField numPlayers = new TextField();
            TextField requiredPoints = new TextField();
            if (!(((TextField) finalDefineGamePopUp.lookup("#name")).getText().equals(""))) {
                name = (TextField) finalDefineGamePopUp.lookup("#name");
            } else {
                defineGamePopUp();
            }
            if (!(((TextField) finalDefineGamePopUp.lookup("#numPlayers")).getText().equals(""))) {
                numPlayers = (TextField) finalDefineGamePopUp.lookup("#numPlayers");
            }else {
                defineGamePopUp();
            }
            if (!(((TextField) finalDefineGamePopUp.lookup("#requiredPoints")).getText().equals(""))) {
                requiredPoints = (TextField) finalDefineGamePopUp.lookup("#requiredPoints");
            }else {
                requiredPoints = null;
            }
            if (requiredPoints != null) {
                games[gameArrayLength] = new Game.GameDefiner(name.getText(), Integer.parseInt(numPlayers.getText())).requiredPoints(Integer.parseInt(requiredPoints.getText())).define();
            } else {
                games[gameArrayLength] = new Game.GameDefiner(name.getText(), Integer.parseInt(numPlayers.getText())).define();
            }
            hidePopUp();
            gameArrayLength++;
        });
    }

    /**
     * SET ACCOUNT LOGGED IN
     * Method to set the logged in account and display appropriate things
     */
    private void setLoggedInAccount(int i) {
        accountLoggedIn = accounts[i];

        if (accountLoggedIn.getType().equals(AccountType.OPERATOR)) {
            optionsBtn.getItems().add(defineGameBtn);
        } else {
            optionsBtn.getItems().add(changeTypeBtn);
        }
        if (accountLoggedIn.getType().equals(AccountType.LEAGUEOWNER)) {
            optionsBtn.getItems().add(createLeagueBtn);
        }
        if (accountLoggedIn.getType().equals(AccountType.ADVERTISER)) {
            tabPane.getTabs().add(myAdvertisementsTab);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

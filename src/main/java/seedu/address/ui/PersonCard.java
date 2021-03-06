package seedu.address.ui;

import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.DisplayGmapEvent;
import seedu.address.commons.events.ui.PersonPanelOptionsDelete;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ReadOnlyPerson;
import java.util.HashMap;
import java.util.Random;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
  
    private static HashMap<String, String> tagColors = new HashMap<String, String>();

    private static String[] colors = { "red", "yellow", "blue", "orange", "brown", "green", "pink", "black", "grey" };
    private static HashMap<String, String> tagColors = new HashMap<String, String>();
    private static Random random = new Random();

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyPerson person;
    private final int displayedIndex;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label birthday;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;
    @FXML
    private MenuButton optionsButton;

    public PersonCard(ReadOnlyPerson person, int displayedIndex) {
        super(FXML);
        this.person = person;
        this.displayedIndex = displayedIndex;
        id.setText(displayedIndex + ". ");
        initTags(person);
        bindListeners(person);
    }

    private static String getColorForTag(String tagValue) {
        if (!tagColors.containsKey(tagValue)) {
            tagColors.put(tagValue, colors[random.nextInt(colors.length)]);
        }
        return tagColors.get(tagValue);
    }

    /**
     * Generate an RGB color then convert to HEX style based on sum of tag's ASCII code. Will regenerate color if sum of
     * RGB is above 700, indicating it is too white (unreadable).
     * This is done so that strings of same value will have persistent colour.
     */
    private static String getColorForTag(String tagValue) {

        if (!tagColors.containsKey(tagValue)) {
            int multiplier = 1;
            int asciiSum = (tagValue.hashCode() > 1) ? tagValue.hashCode() : tagValue.hashCode() * -1;

            int colorRed = asciiSum % 256;
            int colorGreen = (asciiSum / 2) % 256;
            int colorBlue = (asciiSum / 3) % 256;
            while ((colorRed + colorGreen + colorBlue) > 700) {
                asciiSum = (asciiSum / multiplier) * ++multiplier;
                colorRed = asciiSum % 256;
                colorGreen = (asciiSum / 2) % 256;
                colorBlue = (asciiSum / 3) % 256;
            }
            String colorString = String.format("#%02x%02x%02x", colorRed, colorGreen, colorBlue);
            tagColors.put(tagValue, colorString);
        }

        return tagColors.get(tagValue);
    }

    /**
     * Menu list option: Delete
     * Raises PersonPanelOptionsDelete, handled by UIManager
     * Handle Delete user
     */
    @FXML
    public void handleDelete() throws CommandException, ParseException {
        raise(new PersonPanelOptionsDelete(Index.fromOneBased(this.displayedIndex)));
    }

    /**
     * Menu list option: GoogleMap
     * Raises DisplayGmapEvent, handled by BrowserPanel
     * Display google map on main viewport
     */
    @FXML
    public void handleGoogleMap() {
        raise(new DisplayGmapEvent(Index.fromOneBased(this.displayedIndex)));
    }


    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        birthday.textProperty().bind(Bindings.convert(person.birthdayProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        remark.textProperty().bind(Bindings.convert(person.remarkProperty()));
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
          
            initTags(person);
         //   person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
           initTags(Person);
        });
    }

    /**
     * Initialize respective person tag style {@code Person}
     */
    private void initTags(ReadOnlyPerson person) {
        // person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.setStyle("-fx-background-color: " + getColorForTag(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
                }

        );

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);

    }
}

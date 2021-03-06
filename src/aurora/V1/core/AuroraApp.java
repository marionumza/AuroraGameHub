/*
 * Made By Sardonix Creative.
 *
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by-nc-nd/3.0/
 *
 * or send a letter to Creative Commons, 444 Castro Street, ScoreUIte 900,
 * Mountain View, California, 94041, USA.
 * Unless reqcoreUIred by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aurora.V1.core;

import aurora.V1.core.screen_ui.DashboardUI;
import aurora.engine.V1.Logic.AuroraScreenUI;
import aurora.engine.V1.UI.AButton;
import aurora.engine.V1.UI.AImage;
import aurora.engine.V1.UI.ASlickLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JComponent;
import org.apache.log4j.Logger;

/**
 * .---------------------------------------------------------------------------.
 * | AuroraApp
 * .---------------------------------------------------------------------------.
 * |
 * |
 * | AuroraApp is an abstract class extended by classes that represent an
 * | APP in Aurora.
 * |
 * | An APP are simply contained applications that Run on the AuroraGameHub
 * | platform. They are run through the Carousel which may represent many
 * | APPs.
 * |
 * | APPs through AuroraApp implement the AuroraScreenUI interface which allows
 * | for a uniform creation and management of UI.
 * |
 * .............................................................................
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 * @author Carlos Machado <camachado@gmail.com>
 * <p/>
 */
public abstract class AuroraApp implements AuroraScreenUI {

    /**
     * ArrayList containing JComponents to be cleaned of Listeners.
     */
    private ArrayList<JComponent> componentsContainingListeners;

    /**
     * Size Value.
     */
    private int btnBackWidth;

    /**
     * Size Value.
     */
    private int btnBackHeight;

    /**
     * Button found in the FrameControlPanel.
     */
    private AButton btnBack;

    /**
     * Boolean for whether the app is running or not
     */
    public boolean isInApp;

    /**
     * String with the name of the app for reference
     */
    public String appName;

    static final Logger logger = Logger.getLogger(AuroraApp.class);

    private final ASlickLabel lbKeyAction;

    private AImage imgBackIco;

    /**
     * .-----------------------------------------------------------------------.
     * | AuroraApp()
     * .-----------------------------------------------------------------------.
     * |
     * | This is the Constructor of the AuroraApp .
     * |
     * | It simply initializes the componentsContainingListeners ArrayList
     * | to be used in the addToVolatileListenerBank(JComponent) method
     * |
     * .........................................................................
     *
     */
    public AuroraApp() {
        componentsContainingListeners = new ArrayList<JComponent>();


        lbKeyAction = new ASlickLabel();


        lbKeyAction.setForeground(new Color(0, 178, 178));

    }

    /**
     * .-----------------------------------------------------------------------.
     * | addToVolatileListenerBank(JComponent)
     * .-----------------------------------------------------------------------.
     * |
     * | This method collects, in an ArrayList, components that should have
     * | all listeners attached to it removed when going back to Dashboard
     * |
     * | The Volatile Listeners will be removed when going back and should be
     * | re-added when going back into the App.
     * |
     * |
     * .........................................................................
     *
     * @param aComponent JComponent
     *
     */
    public final void addToVolatileListenerBank(final JComponent aComponent) {
        componentsContainingListeners.add(aComponent);
    }

    /**
     * .-----------------------------------------------------------------------.
     * | backToDashboard()
     * .-----------------------------------------------------------------------.
     * |
     * | This method is executed when the App needs to go Back to the Dashboard.
     * | Normally this happens when the User presses the back button on the
     * | FrameControlsPanel.
     * |
     * | The method removes all listeners from components that affect the
     * | DashboardUI using the removeAllListeners() method
     * |
     * | The method clears all changes made to CoreUI before DashboardUI using
     * | the clearUiToDashboard() method.
     * |
     * | The method re-uses the frozen state of DashboardUI instead of
     * | re-creating the Dashboard using addToCanvas()
     * |
     * .........................................................................
     */
    public final void backToDashboard() {


        // let app do its own cleanin up
        closeApp();


        // Clear everything in the Center Panel of CoreUI
        clearUI(false);

        isInApp = false;


        // Re-add all DashboardUI components back to CoreUI
        getDashboardUI().addToCanvas();


        // remove back button
        getCoreUI().getFrameControlImagePane().remove(btnBack);
        getCoreUI().getFrameControlImagePane().setImage(
                "dash_frameControl_bg.png");

        getCoreUI().getFrame().requestFocusInWindow();



    }

    /**
     * .-----------------------------------------------------------------------.
     * | getDashboardUI()
     * .-----------------------------------------------------------------------.
     * |
     * | This method returns the Instanced DashboardUI from the specific APP.
     * |
     * | It is used in the AuroraApp abstract class to take care of going back
     * | and too the Dashboard easily.
     * |
     * .........................................................................
     *
     * <p/>
     * @return DashboardUI
     */
    public abstract DashboardUI getDashboardUI();

    /**
     * .-----------------------------------------------------------------------.
     * | closeApp()
     * .-----------------------------------------------------------------------.
     * |
     * | This method is called when the APP needs to close, while going back
     * | to the Dashboard.
     * |
     * .........................................................................
     *
     * <p/>
     */
    public abstract void closeApp();

    /**
     * .-----------------------------------------------------------------------.
     * | clearUI()
     * .-----------------------------------------------------------------------.
     * |
     * | This method clears the CoreUI to allow any App to use by removing all
     * | content the bottom bar and the center panel as well as ability for the
     * | DashboardUI to be created when going back from any App
     * |
     * | The method must be called by UI Screen classes in the constructor to be
     * | useful before the App UI is added to the CoreUI or When going back to
     * | the Dashboard
     * |
     * | The parameter isClearingApp is used to indicate if the UI is to be
     * | cleaned for an App or the DashboardUI
     * | if clearing CoreUI for an App, set parameter to True, else set to False
     * |
     * .........................................................................
     *
     * @param isClearingApp Boolean
     */
    public final void clearUI(final Boolean isClearingApp) {

        if (logger.isDebugEnabled()) {
            logger.debug("CLEARING " + appName);
        }

        boolean clearForApp;

        if (isClearingApp == null) {
            clearForApp = true;
        } else {
            clearForApp = isClearingApp;
        }

        // Remove all Center and Bottom Bar Content
        getCoreUI().getKeyToPressPanel().removeAll();
        getCoreUI().getCenterPanel().removeAll();
        getCoreUI().getBottomContentPane().removeAll();
        getCoreUI().getCenterFromBottomPanel().removeAll();

        getCoreUI().getKeyToPressPanel().add(Box.createHorizontalStrut(10));



        // Remove All from top of bottom pane and re-add CoreUI components
        getCoreUI().getCenterFromBottomPanel().add(BorderLayout.NORTH,
                                                   getCoreUI()
                                                   .getHeaderOfCenterFromBottomPanel());

        getCoreUI().getHeaderOfCenterFromBottomPanel()
                .setPreferredSize(new Dimension(getCoreUI().getFrame()
                                .getWidth(),
                                                getCoreUI().getKeyToPressPanel()
                                                .getHeight()));

        getCoreUI().getHeaderOfCenterFromBottomPanel().revalidate();

        getCoreUI().getCenterFromBottomPanel().revalidate();
        getCoreUI().getBottomContentPane().revalidate();

        // Remove all from the title pane of the bottom bar and re-add title
        getCoreUI().getTitlePanel().removeAll();
        getCoreUI().getTitlePanel().add(BorderLayout.CENTER, getCoreUI()
                                        .getTitleLabel());

        // Set Size to CoreUI Components
        getCoreUI().getCenterPanel().setPreferredSize(
                new Dimension(
                        getCoreUI().getCenterPanel().getWidth(),
                        getCoreUI().getFrame().getHeight() - getCoreUI()
                        .getBottomPane().getHeight() - getCoreUI().getTopPane()
                        .getHeight() - 5));
        getCoreUI().getCenterPanel().revalidate();

        // Clear Bottom of Top Pane and re-add the Frame Conrols
        getCoreUI().getSouthFromTopPanel().setOpaque(false);
        getCoreUI().getSouthFromTopPanel().removeAll();
        getCoreUI().getSouthFromTopPanel()
                .add(getCoreUI().getFrameControlContainerPanel(),
                     BorderLayout.EAST);

        getCoreUI().getFrame().requestFocus();
        getCoreUI().getFrame().repaint();

        // Remove All Listeners from componentsContainingListeners and Frame
        removeAllListeners();

        if (clearForApp && !isInApp) {
            setUpApp();
        }

        System.gc();

    }

    /**
     * .-----------------------------------------------------------------------.
     * | removeAllListeners()
     * .-----------------------------------------------------------------------.
     * |
     * | This method goes through every JComponent in the ArrayList
     * | componentsContainingListeners as well as the Main JFrame and removes
     * | all possible KeyListeners, MouseWheelListeners as well
     * | as MouseListeners
     * |
     * | This method is used to prevent conflicting handlers being added
     * | In an App and in the DashboardUI.
     * |
     * .........................................................................
     */
    private void removeAllListeners() {

        // Check through all Components in the Volatile Bank!
        for (int i = 0; i < componentsContainingListeners.size(); i++) {
            // Remove KeyListers from Components ArrayList
            KeyListener[] keyListeners = componentsContainingListeners.get(i)
                    .getKeyListeners();

            for (int j = 0; j < keyListeners.length; j++) {
                componentsContainingListeners.get(i)
                        .removeKeyListener(keyListeners[j]);
            }
            // Remove Mouse Listeners from Components ArrayList
            MouseListener[] mouseListeners = componentsContainingListeners
                    .get(i)
                    .getMouseListeners();

            for (int j = 0; j < mouseListeners.length; j++) {
                componentsContainingListeners.get(i)
                        .removeMouseListener(mouseListeners[j]);

            }
            // Remove Mouse Wheel Listeners from Components ArrayList
            MouseWheelListener[] mouseWheelListeners
                                 = componentsContainingListeners
                    .get(i)
                    .getMouseWheelListeners();

            for (int j = 0; j < mouseWheelListeners.length; j++) {
                componentsContainingListeners.get(i)
                        .removeMouseWheelListener(mouseWheelListeners[j]);

            }

            // Maybe its a buttom, try to remove its ActionListeners.
            if (componentsContainingListeners.get(i) instanceof AButton) {
                ActionListener[] actionListeners
                                 = ((AButton) componentsContainingListeners
                        .get(i)).getActionListeners();
                for (int j = 0; j < actionListeners.length; j++) {
                    ((AButton) componentsContainingListeners.get(i))
                            .removeActionListener(actionListeners[j]);
                }
            }

        }
        // Clear Listeners from Frame
        KeyListener[] frameKeyListeners = getCoreUI().getFrame()
                .getKeyListeners();

        for (int i = 0; i < frameKeyListeners.length; i++) {
            getCoreUI().getFrame().removeKeyListener(frameKeyListeners[i]);
        }
        MouseListener[] frameMouseListeners = getCoreUI().getFrame()
                .getMouseListeners();

        for (int i = 0; i < frameMouseListeners.length; i++) {
            getCoreUI().getFrame().removeMouseListener(frameMouseListeners[i]);
        }
        MouseWheelListener[] frameMouseWheelListeners = getCoreUI().getFrame()
                .getMouseWheelListeners();

        for (int i = 0; i < frameMouseWheelListeners.length; i++) {
            getCoreUI().getFrame()
                    .removeMouseWheelListener(frameMouseWheelListeners[i]);
        }

        // Clear Listeners from Frame ContentPane

        KeyListener[] frameContentKeyListeners = getCoreUI().getFrame()
                .getContentPane()
                .getKeyListeners();

        for (int i = 0; i < frameContentKeyListeners.length; i++) {
            getCoreUI().getFrame()
                    .removeKeyListener(frameContentKeyListeners[i]);
        }
        MouseListener[] frameContentMouseListeners = getCoreUI().getFrame()
                .getContentPane()
                .getMouseListeners();

        for (int i = 0; i < frameContentMouseListeners.length; i++) {
            getCoreUI().getFrame()
                    .removeMouseListener(frameContentMouseListeners[i]);
        }
        MouseWheelListener[] frameContentMouseWheelListeners = getCoreUI()
                .getFrame().getContentPane()
                .getMouseWheelListeners();

        for (int i = 0; i < frameContentMouseWheelListeners.length; i++) {
            getCoreUI().getFrame()
                    .removeMouseWheelListener(frameContentMouseWheelListeners[i]);
        }

        componentsContainingListeners.removeAll(componentsContainingListeners);

    }

    private void setSizes() {

        if (getCoreUI().isLargeScreen()) {
            btnBackWidth = 0;
            btnBackHeight = 0;
        } else {

            btnBackWidth = getCoreUI().getExitButtonWidth();
            btnBackHeight = getCoreUI().getExitButtonHeight();
        }
    }

    /**
     * The CoreUI from the specific AuroraScreen.
     * <p/>
     * @return AuroraCoreUI
     */
    public abstract AuroraCoreUI getCoreUI();

    /**
     * .-----------------------------------------------------------------------.
     * | setUpApp()
     * .-----------------------------------------------------------------------.
     * |
     * | This method adds components to the Core UI that prepare the UI for
     * | the APP UI to load.
     * |
     * | It adds a button to the Frame Controls to allow users to go back to the
     * | DashboardUI.
     * |
     * .........................................................................
     */
    public final void setUpApp() {

        setSizes();
        isInApp = true;
        btnBack = new AButton("app_btn_back_norm.png",
                              "app_btn_back_down.png", "app_btn_back_over.png",
                              btnBackWidth, btnBackHeight);
        if (main.LAUNCHES < 20) {
            btnBack.setToolTipText("Back");
        }
        try {
            getCoreUI().getFrameControlImagePane().setImageURL(
                    "dash_frameControlBig_bg.png");
        } catch (MalformedURLException ex) {
            logger.error(ex);
        }

        getCoreUI().getSouthFromTopPanel().setPreferredSize(
                new Dimension(getCoreUI().getSouthFromTopPanel().getWidth(),
                              getCoreUI()
                              .getFrameControlContainerPanel().getHeight()));

        if (logger.isDebugEnabled()) {
            logger.debug("ADDED BACK BUTTON");
        }

        Boolean canAddBackButton = true;
        for (int i = 0; i < getCoreUI().getFrameControlImagePane()
                .getComponents().length; i++) {
            if (getCoreUI().getFrameControlImagePane().getComponents()[i]
                == btnBack) {
                canAddBackButton = false;
            }
        }
        if (canAddBackButton) {
            getCoreUI().getFrameControlImagePane().add(btnBack, 0);
        }

        if (!(btnBack.getMouseListeners()[0] instanceof BackButtonListener)) {
            btnBack.addActionListener(new BackButtonListener());
        }

        // Set Key Icon for gamepad
        if (getCoreUI().isUseGamePad()) {

            lbKeyAction.setFont(getCoreUI().getDefaultFont()
                    .deriveFont(Font.PLAIN,
                                getCoreUI()
                                .getKeysFontSize()));

            imgBackIco = new AImage("KeyboardKeys/b.png", getCoreUI()
                                    .getKeyIconWidth(), getCoreUI()
                                    .getKeyIconHeight());

            lbKeyAction.setText(" Back");

            getCoreUI().getInputController().setListener_B_Button(new BackButtonListener());


            getCoreUI().getKeyToPressPanel().add(imgBackIco);
            getCoreUI().getKeyToPressPanel().add(lbKeyAction);
            getCoreUI().getKeyToPressPanel().add(Box.createHorizontalStrut(6));

        }

        // Ability to go use Escape to go back to dashboard
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(
                        new KeyEventDispatcher() {
                            public boolean dispatchKeyEvent(KeyEvent e) {

                                if (e.getKeyChar() == KeyEvent.VK_ESCAPE && isInApp) {
                                    new BackButtonListener().actionPerformed(null);
                                    return true;
                                }
                                return false;
                            }
                        });
    }

    public class BackButtonListener implements ActionListener {

        public BackButtonListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            removeAllListeners();
            backToDashboard();

        }
    }
}

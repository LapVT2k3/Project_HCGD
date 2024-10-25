package view;

import controller.ServerControl;

/**
 *
 * @author ADMIN
 */
public class ServerView {
    public ServerView() {

        showMessage("TCP Server is running...");
        new ServerControl();
    }
    
    public void showMessage(String msg) {
        System.out.println(msg);
    }
}

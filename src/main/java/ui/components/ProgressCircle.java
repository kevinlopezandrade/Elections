package ui.components;

/* ProgressCircle.java
 *
 * Copyright (C) 2017 Kevin Lopez Andrade <kevin@kevlopez.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import ui.views.MainContainer;
import ui.views.WelcomeView;
import utils.DataManager;
import java.io.IOException;


public class ProgressCircle extends ProgressIndicator {

    public ProgressCircle () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Components/ProgressCircle.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                DataManager dataManager = DataManager.getDataManager();
                return null;
            }
        };

        task.setOnSucceeded((e)->{
            MainContainer.getMainContainer().changeViewTo(WelcomeView.getName());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}

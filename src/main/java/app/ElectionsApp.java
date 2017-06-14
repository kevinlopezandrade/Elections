package app;

/* ElectionsApp.java
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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.components.ProgressCircle;
import ui.views.MainContainer;
import ui.views.WelcomeView;

public class ElectionsApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ProgressCircle progressCircle = new ProgressCircle();
		MainContainer box = MainContainer.getMainContainer();
		WelcomeView welcomeView = new WelcomeView();

		box.registerView(WelcomeView.getName(), welcomeView);
		box.changeViewTo(progressCircle);

		Scene scene = new Scene(box);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}

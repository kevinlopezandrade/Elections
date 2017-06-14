package ui.views;

/* Copyright (C) 2017 Kevin Lopez Andrade <kevin@kevlopez.com>
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

import javafx.scene.layout.*;
import java.util.HashMap;
import javafx.scene.Node;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.animation.FadeTransition;
import javafx.util.Duration;


public class MainContainer extends BorderPane {

	private static MainContainer mainContainer = null;
	private HashMap<String, CustomView> views = new HashMap<>();

	private MainContainer () {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Views/MainContainer.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}


	public static MainContainer getMainContainer () {
		if (mainContainer == null) {
			mainContainer = new MainContainer ();
			return mainContainer;
		}
		else {
			return mainContainer;
		}
	}

	public void changeViewTo (CustomView view) {
		view.refreshView();
		mainContainer.setCenter((Node)view);
		doTransition((Node)view);
	}

	public void changeViewTo (Node view) {
		mainContainer.setCenter(view);
		doTransition(view);
	}

	public void changeViewTo (String viewName) {
		CustomView view = views.get(viewName);
		view.refreshView();
		mainContainer.setCenter((Node)view);
		doTransition((Node)view);
	}

	public void registerView (String key, CustomView view) {
		views.put(key, view);
	}


	private static void doTransition (Node node) {
		FadeTransition ft = new FadeTransition(Duration.millis(250),node);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
	}
}

package utils;

/* ElectionsUtil.java
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

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.util.function.Supplier;

public class ElectionsUtil {

    public static <T, V extends ListCell<T> & Supplier<V> > void allowDeselectGeneric (ListView<T> listView, V cel) {
        listView.setCellFactory(lv -> {
            V cell = cel.get();
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                listView.requestFocus();
                if (! cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (listView.getSelectionModel().getSelectedIndices().contains(index)) {
                        listView.getSelectionModel().clearSelection(index);
                    } else {
                        listView.getSelectionModel().select(index);
                    }
                    event.consume();
                }
            });
            return cell;
        });
    }

}

package ui.components;

/* CustomCheckBox.java
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

import javafx.scene.control.CheckBox;
import utils.CustomSeries;

public class CustomCheckBox extends CheckBox {

    private CustomSeries series;

    public CustomCheckBox() {

        super();
        this.selectedProperty().addListener((ob,oldVal,newVal)->{
            if(newVal) {
                this.series.addToStack();
            }
            else {

                this.series.deleteFromStack();
            }

        });
    }

    public void setSeries (CustomSeries ser) {
        this.series = ser;
    }
}

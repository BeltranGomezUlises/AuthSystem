/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class Testing {

    @BeforeClass //this id done before everything
    public static void beforeClass() {
        //System.out.println("beforeClass");
    }

    @Before //this is done before every @Test
    public void before() {
        //System.out.println("before");
    }

    @Test
    public void generarCGConfigUsers() {
        //System.out.println("generarCGConfigUsers");
    }

    @Test
    public void generarCGConfigMails() {
        //System.out.println("generarCGConfigMails");
    }

    @Test
    public void generarCGConfigGeneral() {
        //System.out.println("generarCGConfigGeneral");
    }

    @After //this is done after every @Test
    public void after() {
        //System.out.println("after");
    }

    @AfterClass //this is done after everything
    public static void afterClass() {
        //System.out.println("afterClass");
    }

}

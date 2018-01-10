package com.apyhs.artoria.beans;

import com.alibaba.fastjson.JSON;
import com.apyhs.artoria.entity.Menu;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DataUtilsTest {

    private List<Menu> list = new ArrayList<Menu>();
    private List<Menu> list1 = new ArrayList<Menu>();

    private void initList() {
        Menu p01 = new Menu();
        p01.setCode("p01");
        p01.setParentCode("");
        list.add(p01);

        Menu p01s01 = new Menu();
        p01s01.setCode("p01s01");
        p01s01.setParentCode("p01");
        list.add(p01s01);

        Menu p01s01s01 = new Menu();
        p01s01s01.setCode("p01s01s01");
        p01s01s01.setParentCode("p01s01");
        list.add(p01s01s01);

        Menu p01s02 = new Menu();
        p01s02.setCode("p01s02");
        p01s02.setParentCode("p01");
        list.add(p01s02);

        Menu p02 = new Menu();
        p02.setCode("p02");
        p02.setParentCode("");
        list.add(p02);
    }

    public void initList1() {
        Menu p01w01 = new Menu();
        p01w01.setCode("p01");
        p01w01.setParentCode("");
        p01w01.setModule("m01");
        list1.add(p01w01);

        Menu p01w02 = new Menu();
        p01w02.setCode("p01");
        p01w02.setParentCode("");
        p01w02.setModule("m02");
        list1.add(p01w02);

        Menu p01w01s01 = new Menu();
        p01w01s01.setCode("p01s01");
        p01w01s01.setParentCode("p01");
        p01w01s01.setModule("m01");
        list1.add(p01w01s01);

        Menu p01w01s02 = new Menu();
        p01w01s02.setCode("p01s02");
        p01w01s02.setParentCode("p01");
        p01w01s02.setModule("m01");
        list1.add(p01w01s02);

        Menu p01w02s01 = new Menu();
        p01w02s01.setCode("p01s01");
        p01w02s01.setParentCode("p01");
        p01w02s01.setModule("m02");
        list1.add(p01w02s01);

        Menu p01w01s01s01 = new Menu();
        p01w01s01s01.setCode("p01s01s01");
        p01w01s01s01.setParentCode("p01s01");
        p01w01s01s01.setModule("m01");
        list1.add(p01w01s01s01);

        Menu p02w01 = new Menu();
        p02w01.setCode("p02");
        p02w01.setParentCode("");
        p02w01.setModule("m01");
        list1.add(p02w01);
    }

    @Before
    public void init() {
        initList();
        initList1();
    }

    @Test
    public void test() {
        System.err.println(JSON.toJSONString(list, true));
        System.err.println(">>>>>>>>>>>>>> multilevel : ");
        List<Menu> vos = DataUtils.multilevelList(list, "sonMenuList", "code", "parentCode");
        System.err.println(JSON.toJSONString(vos, true));
    }

    @Test
    public void test1() {
        System.err.println(JSON.toJSONString(list1, true));
        System.err.println(">>>>>>>>>>>>>> multilevel : ");
        List<Menu> vos = DataUtils.multilevelList(list1, "sonMenuList", "code", "parentCode", "module");
        System.err.println(JSON.toJSONString(vos, true));
    }

}

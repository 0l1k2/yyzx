<template>
      <el-container style="height: 100%;">
     <!-- //左边菜单 -->
      <Aside></Aside>

     <!-- 右边内容 -->
      <el-container direction="vertical">
        <!-- 内容1 -->
      <Header></Header>
        <!-- Tab选项 -->
      <TabNav></TabNav>

        <!--  主题部分-->
        <el-main>
            <router-view></router-view>
        </el-main>
      </el-container>

    </el-container>
</template>

<script setup>  
import Aside from '../../components/Aside.vue';
import Header from '../../components/Header.vue';
import TabNav from '../../components/TabNav.vue';

</script>

<style>
* {
  margin: 0px;
  padding: 0px;
}

body {
  font-size: 14px;
  color: #333333;
}

li {
  list-style: none;
}

a {
  text-decoration: none;
}

html, body, #layout, .el-container, #asideNav, ul.el-menu {
  height: 100%;
}
</style>


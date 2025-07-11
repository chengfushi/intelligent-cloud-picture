import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import UserManagerPage from '@/pages/admin/UserManagerPage.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserProfilePage from '@/pages/user/UserProfilePage.vue'
import AddPicturePage from '@/pages/picture/AddPicturePage.vue'
import PictureManagePage from '@/pages/admin/PictureManagePage.vue'
import PictureDetailPage from '@/pages/picture/PictureDetailPage.vue'
import AddPictureBatchPage from '@/pages/picture/AddPictureBatchPage.vue'
import SpaceManagerPage from '@/pages/admin/SpaceManagerPage.vue'
import AddSpacePage from '@/pages/space/AddSpacePage.vue'
import MySpacePage from '@/pages/space/MySpacePage.vue'
import SpaceDetailPage from '@/pages/space/SpaceDetailPage.vue'
import SearchPicturePage from '@/pages/picture/SearchPicturePage.vue'
import SpaceAnalyzePage from '@/pages/analyze/SpaceAnalyzePage.vue'
import SpaceUserManagePage from '@/pages/space/SpaceUserManagePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      path: '/user/login',
      name: 'login',
      component: UserLoginPage,
    },
    {
      path: '/user/profile',
      name: 'profile',
      component: UserProfilePage,
    },
    {
      path: '/admin/user',
      name: 'admin-user',
      component: UserManagerPage,
    },
    {
      path: '/admin/picture',
      name: 'admin-picture',
      component: PictureManagePage,
    },
    {
      path: '/admin/space',
      name: 'admin-space',
      component: SpaceManagerPage,
    },
    {
      path: '/add_picture',
      name: 'add-picture',
      component: AddPicturePage,
    },
    {
      path: '/add_space',
      name: 'add-space',
      component: AddSpacePage,
    },
    {
      path: '/add_picture/batch',
      name: '批量创建图片',
      component: AddPictureBatchPage,
    },
    {
      path: '/picture/:id',
      name: '图片详情',
      component: PictureDetailPage,
      props: true,
    },
    {
      path: '/my_space',
      name: '我的空间',
      component: MySpacePage,
    },
    {
      path: '/space/:id',
      name: '空间详情',
      component: SpaceDetailPage,
      props: true,
    },
    {
      path: '/search_picture',
      name: '图片搜索',
      component: SearchPicturePage,
    },
    {
      path: '/space_analyze',
      name: '空间分析',
      component: SpaceAnalyzePage,
    },
    {
      path: '/spaceUserManage/:id',
      name: '空间成员管理',
      component: SpaceUserManagePage,
      props: true,
    },
  ],
})

export default router

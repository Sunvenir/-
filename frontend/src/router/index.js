import Vue from 'vue';
import Router from 'vue-router';
import HomePage from '@/HomePage';
import Forum from '@/Forum';
import Player from '@/components/Player';
import testDan from '@/test/testDan';
import testLogin from '@/test/testLogin';
import SearchPage from '@/SearchPage';
import PlayPage from '@/PlayPage'
import UploadPage from '@/UploadPage'
import ManageStudents from '@/ManageStudents'
import ManageUsers from '@/ManageUsers'
import MyCourse from '@/MyCourse'


Vue.use(Router);

export default new Router({
    routes: [
        {
            path:'/',
            name:'HomePage',
            component:HomePage
        },
        {
            path:'/search',
            name:'SearchPage',
            component:SearchPage
        },
        {
            path:'/play',
            name: 'PlayPage',
            component: PlayPage
        },
        {
            path:'/upload',
            name: 'UploadPage',
            component: UploadPage
        },
        {
        	path:'/testVedio',
        	name: 'Player',
        	component: Player
        },
        {
        	path:'/testDan',
        	name: 'testDan',
        	component: testDan
        },
        {
        	path:'/testLogin',
        	name: 'testLogin',
        	component: testLogin
        },
        {
            path:'/Forum',
            name: 'Forum',
            component: Forum
        },
        {
            path:'/manageStudents',
            name: 'ManageStudents',
            component: ManageStudents
        },
        {
            path:'/manageUsers',
            name: 'ManageUsers',
            component: ManageUsers
        },
        {
            path:'/course',
            name: 'MyCourse',
            component: MyCourse
        }
    ]
    // mode:'history'
})

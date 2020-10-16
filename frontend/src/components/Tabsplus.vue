<template>
    <div class="tabsplus">
        <Tabs v-on:changeCourse="changecourse($event)">
        </Tabs>
        <div v-for="item in vedioss">
            <Display
            :title="item.title"
            :cover="item.cover"
            :teacherName="item.teacherName"
            :watches="item.watches"
            :brief="item.brief">
          </Display>
        </div>
    </div>
</template>

<script>
    import Display from "./Display.vue";
    import Tabs from "./Tabs.vue";
    import Tags from "./Tags.vue";
    export default {
        name:"Tabsplus",
        components: {
            Tags,
            Display,
            Tabs
        },
        data() {
            return {
                selected:0,
                vedio:{
                    title: "数学分析1",
                    cover: "/picture/3.jpg",
                    teacherName: "郑孝信",
                    watches: 10000,
                    brief:"兔兔兔兔兔兔兔兔兔",
                },
                vedios:[],
                vedioss:[],
            };
        },
        mounted(){
            for(var j=0;j<=7;j++){
                if(j%2==0)
                    this.vedio.cover="/picture/3.jpg";
                else
                    this.vedio.cover="/picture/1.jpg";
                var vedios1=[];
                for(var i=0;i<=3;i++){
                    var vedio1={
                        title: "数学分析1",
                        cover: "/picture/3.jpg",
                        teacherName: "郑孝信",
                        watches: 10000,
                        brief:"兔兔兔兔兔兔兔兔兔",
                    };
                    this.vedio.title="数学分析"+j+"+"+i;
                    vedio1.title=this.vedio.title;
                    vedio1.cover=this.vedio.cover;
                    vedios1.push(vedio1);
                }
                this.vedios.push(vedios1);
            }
            this.vedioss=this.vedios[this.selected];
        },
        methods:{
            changecourse(i){
                this.selected=i;
                this.vedioss=this.vedios[i];
            },
        }

    };
</script>

<style scoped>
    .tabsplus{
        margin:0 auto;
        width: 800px;
        height:200px;
    }
</style>
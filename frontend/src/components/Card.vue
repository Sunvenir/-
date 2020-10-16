<template>
    <div class="card">
        <div style="height:40px;width: fit-content;line-height: 40px;font-size: 30px;padding-left: 20px">{{title}}</div>
        <div style="height:40px;width: fit-content;opacity: 0.7">
            <div style="height:40px;width: fit-content;float: left;font-size: 15px;line-height: 40px; padding-left: 20px;">
                学习人数 {{studentNumber}}
                <el-divider direction="vertical"></el-divider>
                教师:{{teacherName}}
            </div>
        </div>
        <div style="height:80px;width: 760px;line-height: 25px;font-size: 15px;margin-left: 20px;margin-right:20px;text-align: left;">{{brief}}</div>
    </div>
</template>

<script>
    export default {
        name:"Tags",
        components:{
           
         },
        props:{
            title: String,
            teacherName: String,
            studentNumber: Number,
            brief:String,
        },
        data() {
            return {
               
            };
        },
        methods:{
           
        }
    };
</script>

<style scoped>
    .card{
        background-repeat: no-repeat;
        background-size: cover;
        background-position: inherit;
        width: 800px;
        height:160px;
        margin: 0 auto;
        padding-top: 20px;
        background: #eeeeee;
    }
    .el-divider {
    background-color: grey;
    position: relative;
    }
     .el-divider--vertical {
        display: inline-block;
        width: 2px;
        height: 30px;
        margin: 0 8px;
        vertical-align: middle;
        position: relative;
        color: black;
    }
</style>
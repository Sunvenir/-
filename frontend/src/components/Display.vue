<template>
    <div class="display">
        <el-image style="width: 180px; height: 120px; overflow:initial;" :src="cover" fit="cover"></el-image>
        <div class="inner">
            <div style="color:white;height: 40px;padding-right:5px;padding-left: 10px; margin-top: 20px;font-size: 14px;line-height: 20px;text-align: left;">
                【{{title}}】{{brief}}
            </div>
            <div style="color:white;height: 15px;padding-right:5px;padding-left: 10px; margin-top: 20px;font-size: 14px;line-height: 15px;text-align: left;">
               <el-image style="width: 25px; height: 15px;float: left;" src="/logo/teacher.png" fit="contain"></el-image>{{teacherName}}
            </div>
            <div style="color:white;height: 15px;padding-right:5px;padding-left: 10px; margin-top: 5px;font-size: 14px;line-height: 15px;text-align: left;">
                {{watches}} 播放
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Display",
        props: {
            title: String,
            brief: String,
            cover: String,
            teacherName: String,
            watches: Number,
        },
        data() {
            return {
            };
        },
    };
</script>

<style scoped>
    .display{
        height:120px;
        width:180px;
        overflow:hidden;
        border-radius: 5px;
        margin: 10px;
        float: left;
    }
    .inner{
        height:40px;
        width:180px;
        position:relative;
        margin-top:-40px;
        background:transparent;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        overflow: hidden;
        float: left;
    }
    .inner:hover{
        width: 180px;
        height:120px;
        animation:moveup 0.2s;
        background: rgba(0.1,0.1,0.1,0.7);
        position:relative;
        margin-top:-120px;
        -webkit-line-clamp: 4;
        float: left;
    }
    @keyframes moveup{
        0%   {-webkit-line-clamp: 4;height:40px;background:rgba(0.1,0.1,0.1,0.7); margin-top:-40px;}
        100% {-webkit-line-clamp: 4;height:120px;background:rgba(0.1,0.1,0.1,0.7);margin-top:-120px;}
    }
</style>
<template>
    <div class="display">
        <el-image style="width: 205px;height:134px;overflow:initial;box-shadow:0 0 3px black" :src="cover" fit="cover"></el-image>
        <div class="info">
            <div class="item" style="height:60px;font-weight:bold;line-height:30px;font-size:16px;border-bottom:1px solid #666666">{{title}}</div>
            <div class="item" style="height:18px;font-size:14px;font-weight:bold;color:#555555;">教师：{{teacherName}}</div>
            <div class="item" style="height:18px;font-size:14px;color:#888888;">参与人数：{{par}}人</div>
        </div>
    </div>
</template>

<script>
export default {
    name: "SearchDisplay",
        props: {
        title: String,
        par: String,
        cover: String,
        teacherName: String
    },
    data() {
        return {
        };
    },
};
</script>

<style scoped>
.display{
    width: 205px;
    height: 280px;
    border-radius: 5px;
    background-color: #dddddd;
    overflow: hidden;
}
.info{
    width: 205px;
    height: 146px;
    background-color: #dddddd;
}
.item{
    width: 185px;
    margin-left: 10px;
    margin-top: 10px;
    text-align: left;
    overflow: hidden;
}
</style>
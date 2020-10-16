<template>
    <div class="box">
       <Display
       title="title" 
       breif="breif" 
       cover="cover" 
       teacherName="teacherName"
       watches="watches" 
       proc="proc"
       ></Display>
       <div class="time"></div>
    </div>
</template>

<script>
import Display from './Display.vue'
export default {
    name: "Classbox",
    props: {
        title: String,
        brief: String,
        cover: String,
        teacherName: String,
        watches: Number,
        proc: Number,
        total: Number
    },
    data() {
        return {

        };
    },
};
</script>

<style scoped>

</style>
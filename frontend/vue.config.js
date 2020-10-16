// vue.config.js
module.exports = {
    lintOnSave: false,
    publicPath: './',
    assetsDir: 'public',
    devServer: {
      open:true,
      port:8080,
      proxy: {
        '/': {
            target: 'http://39.106.42.184:8080/',
            changeOrigin: true,
          }
      } 
    }
  }
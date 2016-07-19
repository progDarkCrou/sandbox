/**
 * Created by avorona on 24.06.16.
 */

var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: {
        main: './src/main/app.ts',
        vendor: './src/main/vendor.ts',
        pollyfil: './src/main/pollyfil.ts'
    },
    output: {
        path: './build',
        filename: '[name].bundle.[hash].js'
    },
    resolve: {
      extensions: ['', '.ts', '.js']
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            name: ['main', 'vendor', 'pollyfil']
        }),
        new HtmlWebpackPlugin({
            template: './src/main/index.html',
            // chunks: ['main', 'vendor', 'pollyfil']
        })
    ],
    module: {
        loaders: [
            {
                test: /\.ts$/,
                loader: 'ts'
            },
            {
                test: /\.css$/,
                loader: 'style!css'
            },
            {
                test: /\.less$/,
                loader: 'style!css!less'
            },
            {
                test: /\.html$/,
                loader: 'html'
            }
        ]
    }
};
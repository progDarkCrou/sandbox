/**
 * Created by avorona on 24.06.16.
 */

var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: {
        main: './src/main/app.ts'
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
            name: ['main']
        }),
        new HtmlWebpackPlugin({
            template: './src/main/index.html',
            chunks: ['main']
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
                loader: 'css'
            },
            {
                test: /\.less$/,
                loader: 'css!less'
            }
        ]
    }
};
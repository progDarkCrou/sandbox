/**
 * Created by avorona on 08.06.16.
 */

var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var helpers = require('./conf/helpers');

const ENV = process.env.NODE_ENV = process.env.ENV = 'production';

module.exports = {
    entry: {
        polyfills: './src/main/ts/polyfills.ts',
        vendor: './src/main/ts/vendor.ts',
        app: './src/main/ts/main.ts'
    },
    output: {
        path: './.bundle/',
        filename: '[name].js',
        chunkFilename: '[id].chunk.js'
    },
    resolve: {
        extensions: ['', '.js', '.ts']
    },
    devtool: 'cheap-module-eval-source-map',
    devServer: {
        port: 3000,
        historyApiFallback: true,
        stats: 'minimal'
    },
    module: {
        loaders: [
            {
                test: /\.ts$/,
                loaders: ['ts']
            },
            {
                test: /\.html$/,
                loader: 'html'
            },
            {
                test: /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/,
                loader: 'file?name=assets/[name].[hash].[ext]'
            },
            {
                test: /\.css$/,
                exclude: helpers.root('src', 'app'),
                loader: ExtractTextPlugin.extract('style', 'css?sourceMap')
            },
            {
                test: /\.css$/,
                include: helpers.root('src', 'app'),
                loader: 'raw'
            }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            name: ['app', 'vendor', 'polyfills']
        }),
        new ExtractTextPlugin('[name].css'),
        new HtmlWebpackPlugin({
            template: './src/main/index.html'
        })
    ]
};
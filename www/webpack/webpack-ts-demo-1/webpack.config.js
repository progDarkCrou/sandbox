var path = require('path');
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

var prod = process.env.NODE_ENV === 'prod';

if (prod) {
    module.exports = {
        entry: [
            './src/main/ts/main.ts'
        ],
        output: {
            path: './.build',
            filename: 'bundle.js'
        },
        resolve: {
            extensions: ['', '.webpack.js', '.web.js', '.ts', '.js']
        },
        resolveLoader: {root: path.join(__dirname, 'node_modules')},
        plugins: [
            new webpack.optimize.DedupePlugin(),
            // new webpack.optimize.UglifyJsPlugin(),
            new HtmlWebpackPlugin({
                filename: "index.html",
                template: './src/main/index.html'
            })
        ],
        module: {
            loaders: [
                {
                    test: /\.ts$/,
                    loader: 'ts-loader'
                },
                {
                    test: /\.html$/,
                    loader: "html-minify!html"
                }
            ]
        }
    };
} else {
    module.exports = {
        entry: [
            'webpack/hot/dev-server',
            './src/main/ts/main.ts'
        ],
        output: {
            path: './.build',
            filename: 'bundle.js'
        },
        resolve: {
            extensions: ['', '.webpack.js', '.web.js', '.ts', '.js']
        },
        resolveLoader: {
            root: path.join(__dirname, 'node_modules')
        },
        devtool: 'source-map',
        plugins: [
            new webpack.HotModuleReplacementPlugin(),
            new HtmlWebpackPlugin({
                filename: "index.html",
                template: './src/main/index.html',
                devServer: 'http://localhost:3000'
            })
        ],
        devServer: {
            port: 3000
        },
        module: {
            loaders: [
                {
                    test: /\.html$/,
                    loader: "raw!html-minify"
                },
                {
                    test: /\.ts$/,
                    loader: 'ts-loader'
                }
            ]
        }
    };
}


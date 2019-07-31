
const merge = require('webpack-merge');
const common = require('./webpack.common.js');
const webpack = require('webpack');
const path = require('path');
const environment = require('./environment/environment');
const CleanWebpackPlugin = require('clean-webpack-plugin');

module.exports = merge(common, {
    mode: 'development',
    devtool: 'source-map',
    devServer: {
        hot: false,
        contentBase: './dist',
        port: 4200
    },
    plugins:[
        new webpack.HotModuleReplacementPlugin(),
        new CleanWebpackPlugin(['dist']),
        new webpack.DefinePlugin({
            BASE_URL: JSON.stringify(environment.BASE_URL),
            API1: JSON.stringify(environment.API)
        })
    ],
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'dist'),
    },
    module:{
        rules:[
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.scss$/,
                use: [
                    "style-loader",
                    "css-loader",
                    {
                        loader:"sass-loader",
                        options:{
                            sourceMap: true,
                        },

                    }
                ]
            },
        ]
    },
    optimization: {
        splitChunks: {
            chunks: 'all'
        }
    },

});
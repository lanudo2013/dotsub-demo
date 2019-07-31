

const merge = require('webpack-merge');
const common = require('./webpack.common.js');
const path = require('path');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const MinifyPlugin = require("babel-minify-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");
const environment = require('./environment/environment.prod');
const webpack = require('webpack');

module.exports = merge(common, {
    mode: 'production',
    output: {
        filename: '[name].[contenthash].js',
        path: path.resolve(__dirname, '..', './static'),
        chunkFilename: "[name].[contenthash].js"
    },
    optimization: {
        minimizer:[
            new OptimizeCSSAssetsPlugin({})
        ],
        splitChunks: {
            
            cacheGroups: {
                vendor:{
                    test: /[\\/]node_modules[\\/]/,
                    name: 'vendors',
                    chunks: 'all'
                }
            }
        },
        runtimeChunk: 'single'
    },
    plugins:[
        new MiniCssExtractPlugin({
            filename: "[name].css",
            chunkFilename: "[id].css"
        }),
        new MinifyPlugin({},{}),
        new webpack.DefinePlugin({
            BASE_URL: JSON.stringify(environment.BASE_URL),
            API1: JSON.stringify(environment.API)
        }),
        new CleanWebpackPlugin(['./static'], { root: path.resolve(__dirname, '..') }),
    ],
    module:{
        rules:[
            {
                test: /\.css$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader'
                ]
            },
            {
                test: /\.scss$/,
                use: [
                  {
                        loader: MiniCssExtractPlugin.loader,
                    },
                    {
                        loader: "css-loader",
                        options: {
                            sourceMap: true
                        }
                    },
                    {
                        loader: "sass-loader",
                        options: {
                            sourceMap: true
                        }
                    }


                ]
            }
        ]
    }

});
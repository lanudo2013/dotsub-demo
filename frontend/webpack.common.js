const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');

module.exports = {

    entry: {
        app: './src/index.js',
    },
    plugins: [
        new HtmlWebpackPlugin({
            title: 'Output management',
            template: path.resolve(__dirname, './src/index.html')
        }),
        new webpack.HashedModuleIdsPlugin()
    ],
    resolve: {
        extensions: [ '.ts', '.tsx', '.js' ]
    },
    module:{
        rules:[
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: [
                    {
                        loader: "file-loader",
                        options: {
                            name: '[path][name].[ext]',
                            context: path.resolve(__dirname, "src/assets"),
                            outputPath: 'images/'
                        }
                    }
                ]
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                use: [
                    'file-loader'
                ]
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                }
            },
            {
                test: /\.tsx$/,
                use: {
                    loader: 'ts-loader'
                },
                exclude: /node_modules/
            },
            {
                test: /\.ts$/,
                use: {
                    loader: 'ts-loader'
                },
                exclude: /node_modules/
            }
        ]
    }
};
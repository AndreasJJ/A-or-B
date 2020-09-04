const path = require('path');
const webpack = require('webpack');

const config = {
  devtool: 'eval-source-map',
  entry: [
    'webpack-dev-server/client?http://localhost',
    path.join(__dirname, '/src/index.tsx'),
  ],
  output: {
    path: path.join(__dirname, 'public'),
    filename: 'bundle.js',
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV || 'development'),
    }),
  ],
  mode: 'development',
  devServer: {
    host: '0.0.0.0',
    port: '8081',
    public: 'frontend',
    contentBase: path.join(__dirname, '/public'),
    index: 'index.html',
    historyApiFallback: true,
    hot: true,
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
      'Acccess-Control-Allow-Headers': 'X-Requested-With, content-type, Authorization',
    },
  },
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx'],
  },
  module: {
    rules: [
      { 
        test: /\.tsx?$/,
        exclude: /node_modules/,
        loader: "awesome-typescript-loader" 
      },
      {
        test: /\.jsx?/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: [
              '@babel/preset-env',
              '@babel/preset-react',
            ],
            plugins: [
              require('@babel/plugin-proposal-class-properties'),
              require('@babel/plugin-proposal-object-rest-spread'),
              require('@babel/plugin-transform-destructuring'),
              require('@babel/plugin-proposal-function-bind'),
              require('@babel/plugin-transform-runtime'),
              require('babel-plugin-styled-components'),
            ],
          },
        },
      },
    ],
  },
};
module.exports = config;
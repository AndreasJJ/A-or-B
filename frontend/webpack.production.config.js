const path = require('path');
const webpack = require('webpack');

const config = {
  mode: 'production',
  entry: path.join(__dirname, '/src/index.tsx'),
  output: {
    path: path.join(__dirname, '/public'),
    filename: 'bundle.js',
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV || 'production'),
    }),
  ],
  optimization: {
    namedModules: false,
    namedChunks: false,
    nodeEnv: 'production',
    flagIncludedChunks: true,
    occurrenceOrder: true,
    sideEffects: true,
    usedExports: true,
    concatenateModules: true,
    noEmitOnErrors: true,
    checkWasmTypes: true,
    minimize: true,
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
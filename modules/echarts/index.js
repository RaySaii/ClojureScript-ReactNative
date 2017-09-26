'use strict';

import React from 'react';
import {Image, Text, WebView, View, Platform, Dimensions,ActivityIndicator} from "react-native";


let source;
if (__DEV__) {
    source = require('./echarts.html');
} else {
    source = Platform.OS === 'ios' ? require('./echarts.html') : {uri: 'file:///android_asset/echarts.html'};
}

export default class Echarts extends React.Component {
    static defaultProps = {
        width: Dimensions.get('window').width,
        height: 400,
    }

    componentWillReceiveProps(nextProps) {
        if (!this.loading && !_.isEqual(nextProps.option, this.props.option)) {
            this.refs.chart.reload();
        }
    }

    getOption = (obj) => {
        const escape = `-=remobile=-`;
        return JSON.stringify(obj, function (key, val) {
            if (typeof val === 'function') {
                return `${escape}${val}${escape}`;
            }
            return val;
        }).replace(`"${escape}`, '').replace(`${escape}"`, '').replace(/\\n/g, '');
    }
    getInjectedJavaScript = () => {
        const {width, height, option} = this.props;
        const params = {...option, animation: true};
        return `
        document.body.style.height = "${height}px";
        document.body.style.width = "${width}px";
        echarts.init(document.body).setOption(${this.getOption(params)})
        `;
    }
    reload = () => {
        this.refs.chart.reload();
    }
    onLoadStart = () => {
        this.loading = true;
    }
    onLoadEnd = () => {
        this.loading = false;
    }
    render() {
        const {width, height} = this.props;
        const script = this.getInjectedJavaScript();
        return (
            <View style={{width, height}}>
                <WebView
                    ref='chart'
                    onLoadStart={this.onLoadStart}
                    onLoadEnd={this.onLoadEnd}
                    renderLoading={() => <ActivityIndicator/> }
                    scalesPageToFit={Platform.OS === 'android'}
                    injectedJavaScript={script}
                    style={{width, height}}
                    source={source}
                    scrollEnabled={false}
                />
            </View>
        );
    }
}


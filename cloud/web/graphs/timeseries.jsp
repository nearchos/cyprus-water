<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--
~ Copyright (c) 2019.
~
~ THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR
~  "LICENSE"). THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER
~   THAN AS AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
~
~ BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS
~  LICENSE. TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS
~   CONTAINED HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
~
~ The complete license is available at https://creativecommons.org/licenses/by/3.0/legalcode
-->

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Water level over time</title>
    <!-- including ECharts file -->
    <link rel="stylesheet" href="../css/cyprus-water.css">
    <script src="../js/echarts.min.js"></script>
</head>
<body>

<p class="title">Cyprus Water</p>

<div id="graph" style="width: 100%;height:800px;"></div>

<p>Stacked area graph of water level in all major dams over time</p>

<script>

    let data;

    function updateGraph() {
        let dams = data.dams;
        let percentages = data.percentages;

        let damCapacities = {};
        dams.forEach(function(dam) {
            damCapacities[dam.nameEn] = 1.0 * dam.capacity / 1000000;
        });

        let timestamps = Object.keys(percentages);
        let x = {};
        for(let i in timestamps) {
            let timestamp = timestamps[i];
            x[timestamp] = new Date(timestamp); // convert to JSON timestamp
        }

        let damNames = Object.keys(percentages[timestamps[0]].damNamesToPercentage);
        let damsToTimeSeries = {}; // init empty dictionary
        damNames.forEach(function(dam) {
            let damTimeSeries = [];
            timestamps.forEach(function(timestamp) {
                let damEntry = [x[timestamp], (percentages[timestamp].damNamesToPercentage[dam] * damCapacities[dam]).toFixed(1)];
                damTimeSeries.push(damEntry);
                // damTimeSeries.push((percentages[timestamp].damNamesToPercentage[dam] * damCapacities[dam]).toFixed(1));
            });
            damsToTimeSeries[dam] = damTimeSeries;
        });
        // handle totals
        let totalTimeSeries = [];
        timestamps.forEach(function(timestamp) {
            totalTimeSeries.push((percentages[timestamp].totalPercentage * percentages[timestamp].totalCapacityInMCM).toFixed(1));
        });
        damsToTimeSeries["Total"] = totalTimeSeries;


        // based on prepared DOM, initialize echarts instance
        var myChart = echarts.init(document.getElementById('graph'));

        option = {
            title: {
                text: 'Water level over time'
            },
            tooltip : {
                trigger: 'axis',
                size: '',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data: dams
            },
            toolbox: {
                feature: {
                    saveAsImage: {
                        show: true,
                        // Show the title when mouse focus
                        title: 'Download as picture',
                        name: 'cyprus-water.appspot.org'
                    }
                },
                right: "50px"
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            calculable: true,
            xAxis : [
                {
                    type : 'time',
                    boundaryGap : false,
                    axisLabel: {
                        formatter: (function(value){
                            let date = new Date(value);
                            let month = date.getMonth() + 1;
                            let day = date.getDay();
                            return date.getFullYear() + (month < 10 ? "-0" : "-") + month + (day < 10 ? "-0" : "-") + day;
                        })
                    }
                }
            ],
            yAxis : {},
            series : [
                {
                    name:'Kouris',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Kouris']
                },
                {
                    name:'Asprokremmos',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Asprokremmos']
                },
                {
                    name:'Evretou',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Evretou']
                },
                {
                    name:'Kannaviou',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Kannaviou']
                },
                {
                    name:'Kalavasos',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Kalavasos']
                },
                {
                    name:'Dipotamos',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Dipotamos']
                },
                {
                    name:'Lefkara',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Lefkara']
                },
                {
                    name:'Germasoyeia',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Germasoyeia']
                },
                {
                    name:'Achna',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Achna']
                },
                {
                    name:'Arminou',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Arminou']
                },
                {
                    name:'Polemidia',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Polemidia']
                },
                {
                    name:'Mavrokolympos',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Mavrokolympos']
                },
                {
                    name:'Vyzakia',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Vyzakia']
                },
                {
                    name:'Xyliatos',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Xyliatos']
                },
                {
                    name:'Argaka',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Argaka']
                },
                {
                    name:'Pomos',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Pomos']
                },
                {
                    name:'Kalopanagiotis',
                    type:'line',
                    stack: 'Dams',
                    areaStyle: {normal: {}},
                    data: damsToTimeSeries['Kalopanagiotis']
                },


                {
                    name:'Total',
                    type:'line',
                    stack: 'Total',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {opacity: 0},
                    data: damsToTimeSeries['Total']
                }
            ],
            dataZoom: [
                {
                    type: 'inside'
                },
                {
                    show: true,
                    type: 'slider',
                    startValue: x[0],
                    end: 100,
                    minValueSpan: 10
                }
            ]

        };

        // use configuration item and data specified to show chart
        myChart.setOption(option);
    }

    // execute initially (once) to get data
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            // set the current dam level
            data = JSON.parse(this.responseText);
            updateGraph();
        }
    };
    xhttp.open("GET", "https://cyprus-water.appspot.com/api/timeseries", true);
    xhttp.send();


</script>

</body>
</html>
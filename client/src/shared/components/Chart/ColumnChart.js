import React from 'react';
import { Column } from '@ant-design/plots';
import { Card } from 'antd';

function ColumnChart({ data, title }) {
    const paletteSemanticRed = '#F4664A';
    const brandColor = `rgba(1, 147, 118)`;
    const config = {
        data,
        xField: 'type',
        yField: 'value',
        seriesField: '',
        color: ({ value }) => {
            if (value > 0 && value < 0.25) {
                return paletteSemanticRed;
            }

            return brandColor;
        },
        label: {
            content: (originData) => {
                const val = parseFloat(originData.value);

                if (val < 0.05) {
                    return (val * 100).toFixed(1) + '%';
                }
            },
            offset: 10,
        },
        legend: false,
        xAxis: {
            label: {
                autoHide: true,
                autoRotate: false,
            },
        },
    };
    return (
        <Card title={title} headStyle={{ borderBottom: 'none' }}>
            <Column {...config} />
        </Card>
    );
}

export default ColumnChart;
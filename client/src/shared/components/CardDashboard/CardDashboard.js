import { Card, Space } from 'antd';
import './CardDashboard.scss';

function CardDashboard({ props }) {
    
    return (
        <>
            <div className='card-dashboard'>
                <Card
                    title={
                        <div className='title-card-dashboard'>
                            <Space direction='vertical'>
                                <p className='title'>{props.title.first}</p>
                                <p className='history-title'>({props.title.second})</p>
                            </Space>
                            <img src={props.iconName} alt='' />
                        </div>
                    }>
                    <div className='total'>{props.total}</div>
                </Card>

            </div>
        </>
    );
}

export default CardDashboard;
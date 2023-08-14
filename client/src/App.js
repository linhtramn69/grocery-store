import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { publicRoutes } from './routes/routes';
import './assets/style/GlobalStyle.scss';
function App() {
  return (
    <div className='wrapper'>
      <Router>
        <Routes>
          {
            publicRoutes.map((route, index) => {
              let Layout = route.layout
              return (
                <Route key={index} path={route.path} element={
                  <Layout>
                    <route.component />
                  </Layout>
                } />
              )
            })
          }
        </Routes>
      </Router>
    </div>

  );
}

export default App;
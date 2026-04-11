import { useContext } from 'react';
import cookies from 'react-cookies';
import { MyContext } from '../utils/context/MyContext';
import { useNavigate } from 'react-router-dom';

const Logout = () => {
    const nav = useNavigate();
    const [, dispatch] = useContext(MyContext);
    const logout = () => {
        try {
            console.log("Logging out...");
            cookies.remove('token');
            cookies.remove('refreshToken');
            dispatch({
                'type': 'logout',
                'payload': null
            })
            nav('/login');


        } catch (err) {
            console.error(err);
        }
    }
    return logout;

}

export default Logout;
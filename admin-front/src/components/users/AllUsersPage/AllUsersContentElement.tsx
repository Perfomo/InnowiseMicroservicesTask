
import { useNavigate } from 'react-router-dom';

interface CatalogElementProps {
  username: string;
  userId: string;
  email: string;
  roles: string[]; 
}

const AllUSersContentElement = (props: CatalogElementProps) => {
  const navigate = useNavigate();

  const onClick = () => {
    const username = props.username;
    navigate('/users/' + username + "/show", { state: { value: username } });
  };

  return (
    <div
      className="card text-bg-primary mb-3"
      onClick={onClick}
      style={{
        width: "200px",
        height: "220px",
        margin: "1%",
        textAlign: "center",
      }}
    >
      <div className="card-header" style={{ fontWeight: "700" }}>
        {props.username}
      </div>
      <div className="card-body">
        <p className="card-text">
        {props.userId}
        </p>
        <p>
        {props.email}
        </p>
        <p
          className="card-title"
          style={{ fontWeight: "700", textAlign: "center" }}
        >
          {props.roles}
        </p>
      </div>
    </div>
  );
};

export default AllUSersContentElement;

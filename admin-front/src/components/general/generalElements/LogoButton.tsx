import { ShoppingOutlined } from "@ant-design/icons";
const LogoButton = () => {
  return (
    <nav className="navbar" style={{ marginLeft: "1%", opacity: "1" }}>
      <div className="container-fluid">
        <a className="navbar-brand" href="/menu">
          <ShoppingOutlined /> OnlineShop-Admin
        </a>
      </div>
    </nav>
  );
};

export default LogoButton;

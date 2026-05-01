import { useState } from "react";
import AddProductPage from "./pages/AddProductPage";
import ProductListPage from "./pages/ProductListPage";

function App() {
  const [reload, setReload] = useState(false);

  const refreshProducts = () => {
    setReload(!reload);
  };

  return (
    <div>
      <AddProductPage onProductAdded={refreshProducts} />
      <hr />
      <ProductListPage reload={reload} />
    </div>
  );
}

export default App;
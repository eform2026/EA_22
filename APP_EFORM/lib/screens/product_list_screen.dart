import 'package:flutter/material.dart';

import '../models/product.dart';
import '../services/auth_service.dart';
import 'product_form_screen.dart';

class ProductListScreen extends StatefulWidget {
  const ProductListScreen({super.key});

  @override
  State<ProductListScreen> createState() =>
      _ProductListScreenState();
}

class _ProductListScreenState
    extends State<ProductListScreen> {

  late Future<List<Product>> products;
  late AuthService authService;

  @override
  void initState() {
    super.initState();

    authService = AuthService();
    authService.init().then((_) {
      setState(() {
        products = authService.getProducts();
      });
    });
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(

      appBar: AppBar(
        title: const Text(
          'Lista de Productos',
        ),
      ),

      body: FutureBuilder<List<Product>>(

        future: products,

        builder: (context, snapshot) {

          if (snapshot.connectionState ==
              ConnectionState.waiting) {

            return const Center(
              child:
              CircularProgressIndicator(),
            );
          }

          if (snapshot.hasError) {

            return Center(
              child: Text(
                'Error: ${snapshot.error}',
              ),
            );
          }

          final lista =
              snapshot.data ?? [];

          return ListView.builder(

            itemCount: lista.length,

            itemBuilder:
                (context, index) {

              Product product =
              lista[index];

              return Card(

                margin:
                const EdgeInsets.all(10),

                child: ListTile(

                  title: Text(
                    product.nombre,
                  ),

                  subtitle: Column(
                    crossAxisAlignment:
                    CrossAxisAlignment.start,
                    children: [

                      Text(
                        product.descripcion,
                      ),

                      Text(
                        'Precio: \$${product.precio}',
                      ),

                      Text(
                        'Stock: ${product.stock}',
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),

      floatingActionButton:
      FloatingActionButton(

        onPressed: () {

          Navigator.push(
            context,

            MaterialPageRoute(
              builder: (context) =>
              const ProductFormScreen(),
            ),
          );
        },

        child: const Icon(
          Icons.add,
        ),
      ),
    );
  }
}
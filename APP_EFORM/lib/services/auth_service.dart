import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/product.dart';

class AuthService {
  static const String baseUrl = 'http://localhost:8080';
  static const String tokenKey = 'auth_token';
  static const String userKey = 'user_data';

  late SharedPreferences _prefs;

  // Inicializar preferencias
  Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  // Obtener token guardado
  String? getToken() {
    return _prefs.getString(tokenKey);
  }

  // Obtener datos del usuario guardados
  Map<String, dynamic>? getUserData() {
    final jsonString = _prefs.getString(userKey);
    if (jsonString != null) {
      return jsonDecode(jsonString);
    }
    return null;
  }

  // Verificar si el usuario está autenticado
  bool isAuthenticated() {
    return getToken() != null;
  }

  // LOGIN
  Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/login'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        
        // Guardar token
        await _prefs.setString(tokenKey, data['token']);
        
        // Guardar datos del usuario
        await _prefs.setString(userKey, jsonEncode(data['user']));
        
        return {
          'success': true,
          'message': 'Login exitoso',
          'token': data['token'],
          'user': data['user'],
        };
      } else {
        return {
          'success': false,
          'message': 'Correo o contraseña incorrectos',
        };
      }
    } catch (e) {
      return {
        'success': false,
        'message': 'Error de conexión: $e',
      };
    }
  }

  // REGISTER
  Future<Map<String, dynamic>> register(
    String username,
    String email,
    String password,
  ) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/register'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'username': username,
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 201 || response.statusCode == 200) {
        final data = jsonDecode(response.body);
        
        // Guardar token
        await _prefs.setString(tokenKey, data['token']);
        
        // Guardar datos del usuario
        await _prefs.setString(userKey, jsonEncode(data['user']));
        
        return {
          'success': true,
          'message': 'Registro exitoso',
          'token': data['token'],
          'user': data['user'],
        };
      } else {
        return {
          'success': false,
          'message': 'Error en el registro',
        };
      }
    } catch (e) {
      return {
        'success': false,
        'message': 'Error de conexión: $e',
      };
    }
  }

  // LOGOUT
  Future<void> logout() async {
    await _prefs.remove(tokenKey);
    await _prefs.remove(userKey);
  }

  // Obtener headers con autenticación
  Map<String, String> getAuthHeaders() {
    final token = getToken();
    final headers = {
      'Content-Type': 'application/json',
    };
    
    if (token != null) {
      headers['Authorization'] = 'Bearer $token';
    }
    
    return headers;
  }

  // LISTAR PRODUCTOS
  Future<List<Product>> getProducts() async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/products'),
        headers: getAuthHeaders(),
      );

      if (response.statusCode == 200) {
        List<dynamic> data = jsonDecode(response.body);
        return data.map((item) => Product.fromJson(item)).toList();
      } else if (response.statusCode == 401) {
        // Token expirado o inválido
        await logout();
        throw Exception('Sesión expirada. Por favor, inicie sesión nuevamente.');
      } else {
        throw Exception('Error cargando productos');
      }
    } catch (e) {
      throw Exception('Error: $e');
    }
  }

  // CREAR PRODUCTO
  Future<bool> createProduct({
    required String nombre,
    required String descripcion,
    required double precio,
    required int stock,
  }) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/products'),
        headers: getAuthHeaders(),
        body: jsonEncode({
          'nombre': nombre,
          'descripcion': descripcion,
          'tipoTela': '',
          'imageUrl': '',
          'tallasDisponibles': ['M'],
          'precio': precio,
          'stock': stock,
        }),
      );

      if (response.statusCode == 200 || response.statusCode == 201) {
        return true;
      } else if (response.statusCode == 401) {
        await logout();
        throw Exception('Sesión expirada.');
      } else {
        throw Exception('Error creando producto');
      }
    } catch (e) {
      throw Exception('Error: $e');
    }
  }
}

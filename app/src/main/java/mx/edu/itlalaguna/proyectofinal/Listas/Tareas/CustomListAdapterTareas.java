//package mx.edu.itlalaguna.proyectofinal.Listas.Tareas;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
//import mx.edu.itlalaguna.proyectofinal.R;
//
//public class CustomListAdapterTareas {
//    Context contexto;
//    List<ItemModelTarea> items;
//    LayoutInflater layout;
//
//    public CustomListAdapterTareas ( Context context, List<ItemModelTarea> elementos ) {
//        this.contexto = context;
//        this.items = elementos;
//
//        layout = LayoutInflater.from( contexto );
//    }
//
//    public int getCount () {
//        return items.size();
//    }
//
//    public Object getItem ( int position ) {
//        return items.get( position );
//    }
//
//    public long getItemId ( int position ) {
//        return position;
//    }
//
//    public View getView ( int position, View convertView, ViewGroup parent ) {
//        View view = convertView;
//
//        if ( view == null ) {
//            view = LayoutInflater.from( contexto ).inflate(R.layout.lista_tareas, parent, false );
//        }
//
//        ImageView imagen = view.findViewById( R.id.imgTarea );
//        TextView texto = view.findViewById( R.id.txvTarea );
//
//        ItemModelTarea item = items.get( position );
//
//        texto.setText( item.getNombre() );
//        imagen.setImageResource( ItemModelTarea.getImagen() );
//
//        return view;
//    }
//}

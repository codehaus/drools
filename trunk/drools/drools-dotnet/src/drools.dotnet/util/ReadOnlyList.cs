using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif

namespace org.drools.dotnet.util
{
#if FRAMEWORK11
	/// <summary>
	/// A read-only list that wraps a java.util.List class.
	/// </summary>
	public class ReadOnlyList : IList
#else
	/// <summary>
	/// A read-only list that wraps a java.util.List class.
	/// </summary>
	/// <typeparam name="T">The type of items in the list.</typeparam>
	public class ReadOnlyList<T> : IList<T>
#endif
	{
		private java.util.List _javaList;
#if FRAMEWORK11
		private IList _list = new ArrayList();
#else
		private IList<T> _list = new List<T>();
#endif

		public ReadOnlyList(java.util.List list)
		{
			_javaList = list;
#if FRAMEWORK11
			_list = new ArrayList();
#else
			_list = new List<T>();
#endif
			Refresh();
		}

#if FRAMEWORK11
		public int IndexOf(object item)
#else
		public int IndexOf(T item)
#endif
		{
			return _list.IndexOf(item);
		}

#if FRAMEWORK11
		public void Insert(int index, object item)
#else
		public void Insert(int index, T item)
#endif
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

		public void RemoveAt(int index)
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

#if FRAMEWORK11
		public object this[int index]
#else
		public T this[int index]
#endif
		{
			get { return _list[index]; }
			set { throw new InvalidOperationException("Cannot modify a read-only list."); }
		}

#if FRAMEWORK11
		public int Add(object item)
#else
		public void Add(T item)
#endif
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

		public void Clear()
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

#if FRAMEWORK11
		public bool Contains(object item)
#else
		public bool Contains(T item)
#endif
		{
			return _list.Contains(item);
		}

#if FRAMEWORK11
		public void CopyTo(object[] array, int arrayIndex)
#else
		public void CopyTo(T[] array, int arrayIndex)
#endif
		{
			_list.CopyTo(array, arrayIndex);
		}

		public int Count
		{
			get { return _list.Count; }
		}

		public bool IsReadOnly
		{
			get { return true; }
		}

#if FRAMEWORK11
		public void Remove(object item)
#else
		public bool Remove(T item)
#endif
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

#if FRAMEWORK11
		public IEnumerator GetEnumerator()
#else
		public IEnumerator<T> GetEnumerator()
#endif
		{
			return _list.GetEnumerator();
		}

		System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
		{
			return _list.GetEnumerator();
		}

		public java.util.List List
		{
			get { return _javaList; }
		}

		public void Refresh()
		{
			java.util.Iterator i = _javaList.iterator();
			while (i.hasNext())
			{
#if FRAMEWORK11
				_list.Add(i.next());
#else
				_list.Add((T)i.next());
#endif
			}
		}

#if FRAMEWORK11
		public bool IsFixedSize {get {return true;}}
		public object SyncRoot {get{return _list.SyncRoot;}}
		public bool IsSynchronized  {get{return _list.IsSynchronized ;}}

		public void CopyTo(Array array, int index)
		{
			_list.CopyTo(array, index);
		}
#endif
	}
}

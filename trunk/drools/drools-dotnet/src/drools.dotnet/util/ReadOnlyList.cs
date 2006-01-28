using System;
using System.Collections.Generic;

namespace org.drools.dotnet.util
{
	/// <summary>
	/// A read-only list that wraps a java.util.List class.
	/// </summary>
	/// <typeparam name="T">The type of items in the list.</typeparam>
	public class ReadOnlyList<T> : IList<T>
	{
		private java.util.List _javaList;
		private IList<T> _list = new List<T>();

		public ReadOnlyList(java.util.List list)
		{
			_javaList = list;
			_list = new List<T>();
			Refresh();
		}

		public int IndexOf(T item)
		{
			return _list.IndexOf(item);
		}

		public void Insert(int index, T item)
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

		public void RemoveAt(int index)
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

		public T this[int index]
		{
			get { return _list[index]; }
			set { throw new InvalidOperationException("Cannot modify a read-only list."); }
		}

		public void Add(T item)
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

		public void Clear()
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

		public bool Contains(T item)
		{
			return _list.Contains(item);
		}

		public void CopyTo(T[] array, int arrayIndex)
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

		public bool Remove(T item)
		{
			throw new InvalidOperationException("Cannot modify a read-only list.");
		}

		public IEnumerator<T> GetEnumerator()
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
				_list.Add((T)i.next());
			}
		}
	}
}
